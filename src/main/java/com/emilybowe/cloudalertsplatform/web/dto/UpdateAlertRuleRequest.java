package com.emilybowe.cloudalertsplatform.web.dto;

import com.emilybowe.cloudalertsplatform.domain.Severity;

public record UpdateAlertRuleRequest(
        String description,
        Severity severity,
        Boolean enabled
) {}
