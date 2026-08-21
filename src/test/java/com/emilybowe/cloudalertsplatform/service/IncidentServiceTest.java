package com.emilybowe.cloudalertsplatform.service;

import com.emilybowe.cloudalertsplatform.domain.Incident;
import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    IncidentRepository incidentRepository;

    @InjectMocks
    IncidentService incidentService;

    @Test
    void create() {
        when(incidentRepository.save(any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Incident incident = incidentService.create("overload", Severity.CRITICAL, "system overload", "", null);

        assertThat(incident.getAlertName()).isEqualTo("overload");
        assertThat(incident.getSeverity()).isEqualTo(Severity.CRITICAL);
        assertThat(incident.getSummary()).isEqualTo("system overload");
        assertThat(incident.getDetails()).isEqualTo("");
        assertThat(incident.getRuleId()).isEqualTo(null);

        verify(incidentRepository).save(incident);
    }

    @Test
    void update() {
        UUID id = UUID.randomUUID();
        Incident incident = new Incident("overload", Severity.CRITICAL, "system overload");

        when(incidentRepository.findById(id))
                .thenReturn(Optional.of(incident));

        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.OPEN);
        Incident ackIncident = incidentService.update(id, IncidentStatus.ACKNOWLEDGED, "");
        assertThat(ackIncident.getStatus()).isEqualTo(IncidentStatus.ACKNOWLEDGED);
        assertThat(ackIncident.getAcknowledgedAt()).isNotNull();

        Incident resolvedIncident = incidentService.update(id, IncidentStatus.RESOLVED, "");
        assertThat(resolvedIncident.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
        assertThat(resolvedIncident.getResolvedAt()).isNotNull();
    }

    @Test
    void updateThrows() {
        UUID id = UUID.randomUUID();
        Incident incident = new Incident("overload", Severity.CRITICAL, "system overload");

        when(incidentRepository.findById(id))
                .thenReturn(Optional.of(incident));

        Incident ackIncident = incidentService.update(id, IncidentStatus.ACKNOWLEDGED, "");
        assertThat(ackIncident.getStatus()).isEqualTo(IncidentStatus.ACKNOWLEDGED);

        assertThrows(ConflictException.class, () -> incidentService.update(id, IncidentStatus.OPEN, ""));
    }
}