package com.emilybowe.cloudalertsplatform.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID ruleId;

    @Column(nullable = false, length = 255)
    private String alertName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Severity severity;

    @Column(nullable = false, length = 500)
    private String summary;

    private String details;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant acknowledgedAt;
    private Instant resolvedAt;

    protected Incident() {}

    public Incident(String alertName, Severity severity, String summary) {
        this.alertName = alertName;
        this.severity = severity;
        this.summary = summary;
    }

    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (startedAt == null) startedAt = now;
        status = IncidentStatus.OPEN;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }

}
