# On-call runbook

SLO alerts are evaluated by Prometheus, routed by Alertmanager, and turned into incidents at `POST /api/v1/webhooks/alertmanager`.

Local UIs: Grafana http://localhost:3000, Prometheus http://localhost:9090, Alertmanager http://localhost:9093.

## HighErrorRate

HTTP 500 rate is above 5% for 2 minutes.

**First checks**
- Grafana error-rate panel
- `GET /api/v1/incidents` for an OPEN `HighErrorRate` row
- API logs for 500s (`/demo/error` is the usual cause in this demo)

**Mitigation**
- Stop load against `/demo/error`
- Confirm `/demo/health` and `/actuator/health` return 200
- When the condition clears, Alertmanager sends resolved and the incident should move to RESOLVED

## HighLatencyP95

p95 request latency is above 1s for 2 minutes.

**First checks**
- Grafana p95 panel
- Whether `/demo/slow` is being called
- Postgres: Compose `postgres` healthy; `/actuator/health` includes db

**Mitigation**
- Stop load against `/demo/slow`
- If health is down, fix Postgres / the API before waiting for the alert to resolve
