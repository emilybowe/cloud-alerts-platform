package com.emilybowe.cloudalertsplatform.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alert_rules")
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String service;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Severity severity;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private AlertRule() {}

    public AlertRule(String name, String service, Severity severity) {
        this.name = name;
        this.service = service;
        this.severity = severity;
    }

    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Severity getSeverity() {
        return severity;
    }
}
