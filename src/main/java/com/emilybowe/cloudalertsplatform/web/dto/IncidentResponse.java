package com.emilybowe.cloudalertsplatform.web.dto;

import com.emilybowe.cloudalertsplatform.domain.Incident;
import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;

import java.time.Instant;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        UUID ruleId,
        String alertName,
        IncidentStatus status,
        Severity severity,
        String summary,
        String details,
        Instant startedAt,
        Instant acknowledgedAt,
        Instant resolvedAt,
        Instant createdAt,
        Instant updatedAt
) {

    public static IncidentResponse from (Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getRuleId(),
                incident.getAlertName(),
                incident.getStatus(),
                incident.getSeverity(),
                incident.getSummary(),
                incident.getDetails(),
                incident.getStartedAt(),
                incident.getAcknowledgedAt(),
                incident.getResolvedAt(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }

}


