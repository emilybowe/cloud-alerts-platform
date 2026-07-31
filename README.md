# Cloud Alerts Platform

Alert management service built as a portfolio project: REST API for alert rules and incident lifecycle, backed by PostgreSQL, with Prometheus, Grafana, and Alertmanager for SLO-style alerting.

## Stack

Java 17 · Spring Boot · PostgreSQL · Flyway · Micrometer · Docker Compose · (later) AWS + Terraform

## Status

Early scaffold. Milestone 1 in progress: rules/incidents API, Flyway schema, demo endpoints, and `docker compose` for API + Postgres.

## Quick start

```bash
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
