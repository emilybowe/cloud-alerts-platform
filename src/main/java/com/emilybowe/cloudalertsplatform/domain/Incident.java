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
        this.status = IncidentStatus.OPEN;
    }

    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (startedAt == null) startedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public String getAlertName() {
        return alertName;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getSummary() {
        return summary;
    }

    public String getDetails() {
        return details;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void updateSeverity(Severity severity) {
        this.severity = severity;
    }

    public void updateStatus(IncidentStatus status) {
        this.status = status;
        Instant now = Instant.now();
        if (status == IncidentStatus.ACKNOWLEDGED) updateAcknowledgedAt(now);
        if (status == IncidentStatus.RESOLVED) updateResolvedAt(now);
    }

    public void updateDetails(String details) {
        this.details = details;
    }

    public void updateRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    private void updateAcknowledgedAt(Instant acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    private void updateResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
