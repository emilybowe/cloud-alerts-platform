package com.emilybowe.cloudalertsplatform.web.dto;

import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;

public record UpdateIncidentRequest(
        IncidentStatus status,
        String details
) {
}
