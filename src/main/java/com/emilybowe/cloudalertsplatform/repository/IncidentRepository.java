package com.emilybowe.cloudalertsplatform.repository;

import com.emilybowe.cloudalertsplatform.domain.Incident;
import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    Optional<Incident> findFirstByAlertNameAndStatusIn(
            String alertname,
            Collection<IncidentStatus> statuses
    );

    Long countByStatusInAndSeverity(
            Collection<IncidentStatus> statuses,
            Severity severity
    );
}
