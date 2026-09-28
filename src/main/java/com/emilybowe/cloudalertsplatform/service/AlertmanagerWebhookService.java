package com.emilybowe.cloudalertsplatform.service;

import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.IncidentRepository;
import com.emilybowe.cloudalertsplatform.web.dto.AlertmanagerWebhookRequest;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class AlertmanagerWebhookService {

    private static final List<IncidentStatus> ACTIVE = List.of(IncidentStatus.OPEN, IncidentStatus.ACKNOWLEDGED);

    private final IncidentRepository incidentRepository;
    private final IncidentService incidentService;
    private final MeterRegistry meterRegistry;

    public AlertmanagerWebhookService(
            IncidentRepository incidentRepository,
            IncidentService incidentService,
            MeterRegistry meterRegistry
    ) {
        this.incidentRepository = incidentRepository;
        this.incidentService = incidentService;
        this.meterRegistry = meterRegistry;
    }

    public void handle(AlertmanagerWebhookRequest request) {
        String status = request.status() != null ? request.status() : "unknown";
        meterRegistry.counter("alertmanager.webhooks.received", "status", status).increment();

        if (request.alerts() == null) return;

        for(AlertmanagerWebhookRequest.AlertmanagerAlert alert : request.alerts()) {
            handleAlert(alert);
        }
    }

    private void handleAlert(AlertmanagerWebhookRequest.AlertmanagerAlert alert) {
        if (alert.labels() == null) return;
        String alertname = alert.labels().get("alertname");
        if (alertname == null || alertname.isBlank()) return;
        String status = alert.status();
        if ("firing".equalsIgnoreCase(status)) {
            handleFiring(alertname, alert);
        } else if ("resolved".equalsIgnoreCase(status)) {
            handleResolved(alertname);
        }
    }

    private void handleFiring(String alertname, AlertmanagerWebhookRequest.AlertmanagerAlert alert) {
        if (incidentRepository.findFirstByAlertNameAndStatusIn(alertname, ACTIVE).isPresent()) return;

        Severity severity = getSeverity(alert);
        String details = getDetails(alert);
        String summary = getSummary(alert);

        incidentService.create(alertname, severity, summary, details, null);

    }

    private void handleResolved(String alertname) {
        incidentRepository.findFirstByAlertNameAndStatusIn(alertname, ACTIVE)
                .ifPresent(incident ->
                        incidentService.update(incident.getId(), IncidentStatus.RESOLVED, null));
    }

    private static Severity getSeverity(AlertmanagerWebhookRequest.AlertmanagerAlert alert) {
        if (alert.labels() != null) {
            String severity = alert.labels().get("severity");
            if (severity != null && !severity.isBlank() && severity.equalsIgnoreCase("critical")) {
                return Severity.CRITICAL;
            }
        }
        return Severity.WARNING;
    }

    private static String getSummary(AlertmanagerWebhookRequest.AlertmanagerAlert alert) {
        if (alert.annotations() != null) {
            String summary = alert.annotations().get("summary");
            if (summary != null && !summary.isBlank()) {
                return truncate(summary, 500);
            }
        }
        return truncate(alert.labels().get("alertname"), 500);
    }

    private static String getDetails(AlertmanagerWebhookRequest.AlertmanagerAlert alert) {
        return "labels=" + alert.labels() + "; annotations=" + alert.annotations();
    }

    private static String truncate(String value, int max) {
        if (value == null) return "";
        return value.length() <= max ? value : value.substring(0, max);
    }
}
