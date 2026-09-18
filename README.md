# Cloud Alerts Platform

Alert management service built as a portfolio project: REST API for alert rules and incident lifecycle, backed by PostgreSQL, with Prometheus, Grafana, and Alertmanager for SLO-style alerting.

## Stack

Java 25 · Spring Boot · PostgreSQL · Flyway · Micrometer · Docker Compose · (later) AWS + Terraform

## Status

Milestone 2 observability loop is runnable via Compose: scrape → Grafana → firing alerts → incidents.

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

## Architecture

```
Client ──▶ Spring Boot API ──▶ PostgreSQL
                │
                ▼
         Prometheus ──▶ Alertmanager ──webhook──▶ API (incidents)
                │
                ▼
             Grafana
```

## Observability

| UI | URL | Notes |
|----|-----|--------|
| Grafana | http://localhost:3000 | Dashboard **Cloud Alerts Platform** is provisioned; no import. |
| Prometheus | http://localhost:9090 | Alerts → Pending / Firing |
| Alertmanager | http://localhost:9093 | Routes to `POST /api/v1/webhooks/alertmanager` |

On-call steps: [docs/runbook.md](docs/runbook.md).

Trigger `HighErrorRate` (keep this running **>2 minutes** — rules use `for: 2m`):
```shell
while true; do curl -s -o /dev/null "http://localhost:8080/demo/error?rate=0.5"; sleep 0.2; done
```
Then:
```shell
curl -s http://localhost:8080/api/v1/incidents
```

You should see an OPEN incident with alertName HighErrorRate.
Stop the loop; after the alert clears, Alertmanager sends resolved and that incident should become RESOLVED.

## Development

```shell
./mvnw test
```

Pushes and PRs to main run `./mvnw test` and build the image in `.github/workflows/ci.yml`

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
