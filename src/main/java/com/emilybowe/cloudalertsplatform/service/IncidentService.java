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
                .orElseThrow(() -> new ResourceNotFoundException (
                        "Incident not found for ID: " + id));
    }

    public List<Incident> list() {
        return incidentRepository.findAll();
    }

    public Incident updateStatus(UUID id, IncidentStatus status) {
        Incident incident = this.getById(id);
        IncidentStatus currentStatus = incident.getStatus();
        if (isValidStatus(incident, status)) {
            incident.updateStatus(status);
        } else throw new ConflictException("Incident status from %s to %s invalid".formatted(currentStatus, status));
        return incident;
    }

    public Incident updateDetails(UUID id, String details) {
        Incident incident = this.getById(id);
        if (details != null) incident.updateDetails(details);
        return incident;
    }

    public void delete(UUID id) {
        Incident incident = this.getById(id);
        incidentRepository.delete(incident);
    }

    private boolean isValidStatus(Incident incident, IncidentStatus newStatus) {
        if (newStatus == null) return false;
        IncidentStatus currentStatus = incident.getStatus();
        switch (currentStatus) {
            case OPEN -> {
                if (newStatus == IncidentStatus.ACKNOWLEDGED) return true;
                if (newStatus == IncidentStatus.RESOLVED) return true;
            }
            case ACKNOWLEDGED -> {
                if (newStatus == IncidentStatus.RESOLVED) return true;
            }
        }
        return false;
    }

}
