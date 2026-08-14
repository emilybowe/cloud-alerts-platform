package com.emilybowe.cloudalertsplatform.web.dto;

import com.emilybowe.cloudalertsplatform.domain.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAlertRuleRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String service,
        String description,
        @NotNull Severity severity,
        Boolean enabled
) {}
