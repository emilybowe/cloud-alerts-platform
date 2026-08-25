# Cloud Alerts Platform

Alert management service built as a portfolio project: REST API for alert rules and incident lifecycle, backed by PostgreSQL, with Prometheus, Grafana, and Alertmanager for SLO-style alerting.

## Stack

Java 25 · Spring Boot · PostgreSQL · Flyway · Micrometer · Docker Compose · (later) AWS + Terraform

## Status

Milestone 1 local API is runnable via Compose.

## Quick start

```shell
cp .env.example .env
# set POSTGRES_USER and POSTGRES_PASSWORD

docker compose up --build
```

```shell
curl -i http://localhost:8080/actuator/health
curl -i http://localhost:8080/demo/health
```
Expect response UP from actuator and response ok from demo.

Create rule (201):
```shell
curl -i http://localhost:8080/api/v1/rules \
  -H "Content-Type: application/json" \
  -d '{
    "name": "high-error-rate",
    "service": "cloud-alerts-platform",
    "description": "HTTP 5xx rate above threshold",
    "severity": "CRITICAL",
    "enabled": true
  }'
```
List / get / patch rule: `GET /api/v1/rules`, `GET /api/v1/rules/{id}` `PATCH` `{"enabled": false}` & `"severity": "WARNING"`

Create incident (201):
```shell
curl -i http://localhost:8080/api/v1/incidents \
  -H "Content-Type: application/json" \
  -d '{
    "alertName": "high-error-rate",
    "severity": "CRITICAL",
    "summary": "Error rate above threshold"
  }'
```
`ruleId` and `details` are optional.

Change status:
```shell
curl -i -X PATCH http://localhost:8080/api/v1/incidents/{id} \
  -H "Content-Type: application/json" \
  -d '{"status": "ACKNOWLEDGED"}'
```
Resolve: same `PATCH` with `RESOLVED`. Note that ack → OPEN is 409.
List: `GET /api/v1/incidents`

Demo endpoints:
`GET /demo/health` — response 200
`GET /demo/slow?ms=500` — delay
`GET /demo/error?rate=0.5` — response mix of 200/500

## Development

```shell
./mvnw test
```


Full local stack and curl examples will land with Milestone 1.

## Design

See [design/overview.md](design/overview.md) for architecture, domain model, and API outline.

## Roadmap

1. **Local API** — CRUD, tests, Compose  
2. **Observability** — metrics, alerts, webhook → incidents  
3. **CI & AWS** — GitHub Actions, Terraform deploy  

## Related

Companion repos (separate): `incident-copilot` (LLM sidecar), `reliability-lab` (k6 / fault injection).

## License

MIT
