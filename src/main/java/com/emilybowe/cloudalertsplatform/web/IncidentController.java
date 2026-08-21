package com.emilybowe.cloudalertsplatform.web;

import com.emilybowe.cloudalertsplatform.service.IncidentService;
import com.emilybowe.cloudalertsplatform.web.dto.CreateIncidentRequest;
import com.emilybowe.cloudalertsplatform.web.dto.IncidentResponse;
import com.emilybowe.cloudalertsplatform.web.dto.UpdateIncidentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping("/api/v1/incidents")
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponse create(@Valid @RequestBody CreateIncidentRequest request) {
        return IncidentResponse.from(incidentService.create(
                request.alertName(),
                request.severity(),
                request.summary(),
                request.details(),
                request.ruleId()
        ));
    }

    @PatchMapping("/api/v1/incidents/{id}")
    public IncidentResponse update(
            @PathVariable UUID id,
            @RequestBody UpdateIncidentRequest request
    ) {
        return IncidentResponse.from(incidentService.update(
                id,
                request.status(),
                request.details()

        ));
    }

    @GetMapping("/api/v1/incidents/{id}")
    public IncidentResponse getById(@PathVariable UUID id) {
        return IncidentResponse.from(incidentService.getById(id));
    }

    @GetMapping("/api/v1/incidents")
    public List<IncidentResponse> list() {
        return incidentService.list().stream()
                .map(IncidentResponse::from)
                .toList();
    }
}
