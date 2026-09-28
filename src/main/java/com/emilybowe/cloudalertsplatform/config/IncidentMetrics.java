package com.emilybowe.cloudalertsplatform.config;

import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.IncidentRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentMetrics implements MeterBinder {

    private final IncidentRepository incidentRepository;

    private static final List<IncidentStatus> ACTIVE = List.of(
            IncidentStatus.OPEN,
            IncidentStatus.ACKNOWLEDGED
    );

    public IncidentMetrics(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        for (Severity severity : Severity.values()) {
            meterRegistry.gauge(
                    "incidents.active",
                    Tags.of("severity", severity.name()),
                    incidentRepository,
                    repo -> repo.countByStatusInAndSeverity(ACTIVE, severity)
            );
        }
    }
}
