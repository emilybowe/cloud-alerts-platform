package com.emilybowe.cloudalertsplatform.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlertmanagerWebhookRequest(
        String status,
        List<AlertmanagerAlert> alerts
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlertmanagerAlert(
            String status,
            Map<String, String> labels,
            Map<String, String> annotations
    ) {}
}
