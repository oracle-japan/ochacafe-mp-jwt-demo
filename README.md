# OCHaCafe Season4 - #4 マイクロサービスの認証・認可と JWT

[MicroProfile JWT RBAC](https://download.eclipse.org/microprofile/microprofile-jwt-auth-1.2/microprofile-jwt-auth-spec-1.2.html) を使用したマイクロサービスの認証・認可のデモのソースコードです．

## デモ構成

```txt
.
├── README.md
├── auth [ユーザー認証用サービス(OIDC + FIDO)]
│   ├── Dockerfile
│   ├── README.md
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── me
│       │   │       └── shukawam
│       │   │           └── example
│       │   │               └── mp
│       │   │                   └── auth
│       │   │                       ├── AuthResource.java [ユーザー認証エンドポイント定義]
│       │   │                       └── package-info.java
│       │   └── resources
│       │       ├── META-INF
│       │       │   ├── beans.xml
│       │       │   ├── microprofile-config.properties
│       │       │   └── native-image
│       │       │       └── reflect-config.json
│       │       ├── application.yaml [アプリケーションの設定ファイル(IdP URI etc.)]
│       │       └── logging.properties
│       └── test
│           └── java
│               └── me
│                   └── shukawam
│                       └── example
│                           └── mp
│                               └── auth
│                                   └── MainTest.java
├── deploy
│   └── kubernetes [Kubernetes デプロイメント用 Manifest]
│       ├── base
│       │   ├── auth.yaml
│       │   ├── event.yaml
│       │   ├── kustomization.yaml
│       │   ├── mandatory.yaml
│       │   └── mp-jwt-ingress.yaml
│       └── overlays
│           └── kustomization.yaml
└── event [認証・認可制御対象アプリケーション]
    ├── Dockerfile
    ├── README.md
    ├── pom.xml
    └── src
        ├── main
        │   ├── java
        │   │   └── me
        │   │       └── shukawam
        │   │           └── example
        │   │               └── mp
        │   │                   └── event
        │   │                       ├── CreateEventRequest.java
        │   │                       ├── Event.java
        │   │                       ├── EventResource.java [エンドポイント・アクセス制御定義]
        │   │                       ├── EventService.java
        │   │                       └── package-info.java
        │   └── resources
        │       ├── META-INF
        │       │   ├── beans.xml
        │       │   ├── init_script.sql
        │       │   ├── microprofile-config.properties
        │       │   ├── native-image
        │       │   │   ├── native-image.properties
        │       │   │   └── reflect-config.json
        │       │   └── persistence.xml
        │       ├── application.yaml [アプリケーション設定ファイル(IdP URI, Database connection settings)]
        │       ├── hibernate.properties
        │       └── logging.properties
        └── test
            └── java
                └── me
                    └── shukawam
                        └── example
                            └── mp
                                └── event
                                    └── MainTest.java
```

本サンプルアプリケーションは、 [Helidon - Security Provider(OidcProvider, IDCSRoleMapper)](https://oracle-japan-oss-docs.github.io/helidon/docs/v2/#/mp/security/02_providers#_idcs_role_mapper) を用いてマイクロサービスの認証・認可を実現しています．`auth/src/resources/application.yaml`, `event/src/resources/application.yaml`に当該機能の設定が記載されています．以下の部分を自身の環境に合わせて修正してください．(環境変数に設定することを推奨します)
下記以外のパラメーターに関しては [Helidon MP - Security Provider(OIDC Provider)](https://oracle-japan-oss-docs.github.io/helidon/docs/v2/#/mp/security/02_providers#_oidc_provider) をご参照ください．

```yaml
# Security config
security:
  # Set to true for production - if set to true, clear text passwords will cause failure
  config:
    require-encryption: false
  properties:
    # Identity Provider - IDCS
    idcs-uri: <your-idcs-tenant-uri>
    idcs-client-id: <your-idcs-client-id>
    idcs-client-secret: <your-idcs-client-secret>
    frontend-uri: <your-front-end>
    proxy-host:
```

## アクセス制御概要

マイクロサービス (event) のエンドポイントに対して RBAC で制御をかけています．

| Path                       | Role         |
| -------------------------- | ------------ |
| GET /event                 | Admin, Guest |
| GET /event/id/{id}         | Admin, Guest |
| GET /event/season/{season} | Admin, Guest |
| POST /event                | Admin        |
| DELETE /event/id/{id}      | Admin        |

`http://<auth-host>:<auth-port>/auth/login`へアクセスし、認証情報を入力するとブラウザに OAuth 2.0 のアクセストークンが返却されるので、その取得したアクセストークンを用いて event サービスに対して API リクエストを行います．

### 例

リクエスト；

```bash
curl --request GET --url http://<event-host>:<event-port>/event/id/215000 --header 'authorization: Bearer eyJ...'
```

レスポンス(Role: Guest)；

```http
HTTP/1.1 200 OK
Server: nginx/1.17.8
Date: Tue, 22 Jun 2021 08:40:00 GMT
Content-Type: application/json
Content-Length: 127
Connection: close

{
  "eventDate": "2021-07-07Z",
  "id": 215000,
  "presenter": "k",
  "season": "4",
  "title": "マイクロサービスの認証・認可とJWT"
}
```

リクエスト；

```bash
curl --request POST --url http://<event-host>:<event-port>/event --header 'authorization: Bearer eyJ...' --header 'content-type: application/json' --data '{"title": "Kubernetesのオートスケーリング","season": "4","presenter": "n","eventDate": "2020-08-11"}'
```

レスポンス(Role: Guest)；

```http
HTTP/1.1 403 Forbidden
Server: nginx/1.17.8
Date: Tue, 22 Jun 2021 08:42:18 GMT
Content-Length: 0
Connection: close
```

レスポンス(Role: Admin)；

```http
HTTP/1.1 200 OK
Server: nginx/1.17.8
Date: Tue, 22 Jun 2021 12:18:58 GMT
Content-Type: application/json
Content-Length: 119
Connection: close

{
  "eventDate": "2020-01-11Z",
  "id": 216000,
  "presenter": "n",
  "season": "4",
  "title": "Kubernetesのオートスケーリング"
}
```
