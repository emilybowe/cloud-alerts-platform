# Cloud Alerts Platform — Design Overview

Production-style alert management API for a portfolio demo: define alert rules, track incidents through a lifecycle, and wire SLO alerts via Prometheus, Grafana, and Alertmanager.

## Goals

- REST API for rules and incidents (CRUD + ack/resolve)
- PostgreSQL persistence with Flyway migrations
- Prometheus metrics and Grafana dashboards
- Alertmanager webhook creates/updates incidents
- Local demo via Docker Compose; optional AWS deploy later

## Non-goals

Auth, web UI, multi-tenancy, Kafka, and real PagerDuty. Single Spring Boot service only.

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

## Domain

**AlertRule** — name, service, severity (`WARNING` | `CRITICAL`), enabled flag.

**Incident** — linked rule (optional), alert name, status (`OPEN` → `ACKNOWLEDGED` → `RESOLVED`), severity, summary/details, timestamps. Invalid transitions return 409.

## API (base `/api/v1`)

- `/rules` — create, list, get, patch, delete
- `/incidents` — create, list, get, patch (ack/resolve)
- `/webhooks/alertmanager` — firing creates OPEN incidents; resolved closes matches
- `/demo/slow`, `/demo/error`, `/demo/health` — load/alert test endpoints
- Actuator: `/actuator/health`, `/actuator/prometheus`

## Stack

Java 17, Spring Boot, JPA, Flyway, PostgreSQL, Micrometer, Docker Compose; later Terraform + ECS/EKS + RDS.

## Milestones

1. Local API + Postgres + tests  
2. Observability loop (Prometheus/Grafana/Alertmanager)  
3. CI + AWS deploy
