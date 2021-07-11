# auth

Helidon MP application that uses SecurityProvider for login with IDCS.

## Build and run

With JDK11+

```bash
mvn package -DskipTests
java -jar target/auth.jar
```

With Helidon CLI (dev mode)

```bash
helidon dev
```

## Exercise the application

```bash
# Login with OIDC via IDCS
curl -X GET http://localhost:7987/auth/login
# Call OIDC userinfo endpoint
curl -X GET http://localhost:7987/auth/profile --header 'authorization: Bearer <access_token>'
```

## Try health and metrics

```bash
curl -s -X GET http://localhost:7987/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:7987/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:7987/metrics
{"base":...
. . .
```
