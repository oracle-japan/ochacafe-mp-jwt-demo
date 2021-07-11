# event

Helidon MP application that uses JPA with an Autonomous Transaction Processing(ATP).

## Build and run

With JDK11+

```bash
mvn package -DskipTests
java -jar target/event.jar
```

With Helidon CLI (dev mode)

```bash
helidon dev
```

## Exercise the application

```bash
# Get all event that stored in ATP.
curl -X GET http://localhost:8080/event --header 'authorization: Beaere <access_token>'
[{"eventDate":"2021-07-07Z","id": 215000,"presenter":"k","season":"4","title":"マイクロサービスの認証・認可とJWT"}, ...]

# Get specific season event that stored in ATP.
curl -X GET http://localhost:8080/event/season/4 --header 'authorization: Beaere <access_token>'
[{"eventDate":"2021-07-07Z","id": 215000,"presenter":"k","season":"4","title":"マイクロサービスの認証・認可とJWT"}, ...]

# Create a new event.
curl -H "Content-Type: application/json" -H 'authorization: Beaere <access_token>' --request POST --data '{"title": "Kubernetesのオートスケーリング","season": "4","presenter": "n","eventDate": "2020-08-11"}' http://localhost:8080/event
```

## Try health and metrics

```bash
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .
```
