package com.emilybowe.cloudalertsplatform.web.dto;

import com.emilybowe.cloudalertsplatform.domain.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateIncidentRequest(
        @NotBlank @Size(max = 255) String alertName,
        @NotNull Severity severity,
        @NotBlank @Size(max = 500) String summary,
        String details,
        UUID ruleId
) {
}
