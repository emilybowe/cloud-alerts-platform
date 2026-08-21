package com.emilybowe.cloudalertsplatform.service;

import com.emilybowe.cloudalertsplatform.domain.Incident;
import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Incident create(String alertName, Severity severity, String summary, String details, UUID ruleId) {
        Incident incident = new Incident(alertName, severity, summary);
        if (details != null) incident.updateDetails(details);
        if (ruleId != null) incident.updateRuleId(ruleId);
        incidentRepository.save(incident);
        return incident;
    }

    public Incident getById(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Incident not found for ID: " + id));
    }

    public List<Incident> list() {
        return incidentRepository.findAll();
    }

    public Incident update(UUID id, IncidentStatus status, String details) {
        Incident incident = this.getById(id);
        if (status != null) {
            if (isValidStatus(incident, status)) {
                incident.updateStatus(status);
            } else {
                IncidentStatus current = incident.getStatus();
                throw new ConflictException("Incident status from %s to %s invalid".formatted(current, status));
            }
        }
        if (details != null) incident.updateDetails(details);
        return incident;
    }

    private boolean isValidStatus(Incident incident, IncidentStatus status) {
        IncidentStatus current = incident.getStatus();
        switch (current) {
            case OPEN -> {
                if (status == IncidentStatus.ACKNOWLEDGED) return true;
                if (status == IncidentStatus.RESOLVED) return true;
            }
            case ACKNOWLEDGED -> {
                if (status == IncidentStatus.RESOLVED) return true;
            }
        }
        return false;
    }

}
