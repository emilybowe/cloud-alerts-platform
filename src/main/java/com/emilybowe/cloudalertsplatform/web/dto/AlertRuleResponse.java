package com.emilybowe.cloudalertsplatform.web.dto;

import com.emilybowe.cloudalertsplatform.domain.AlertRule;
import com.emilybowe.cloudalertsplatform.domain.Severity;

import java.time.Instant;
import java.util.UUID;

public record AlertRuleResponse(
        UUID id,
        String name,
        String service,
        String description,
        Severity severity,
        Boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
    public static AlertRuleResponse from(AlertRule rule) {
        return new AlertRuleResponse(
                rule.getId(),
                rule.getName(),
                rule.getService(),
                rule.getDescription(),
                rule.getSeverity(),
                rule.getEnabled(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }
}
