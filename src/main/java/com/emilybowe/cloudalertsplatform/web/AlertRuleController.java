package com.emilybowe.cloudalertsplatform.web;

import com.emilybowe.cloudalertsplatform.service.AlertRuleService;
import com.emilybowe.cloudalertsplatform.web.dto.AlertRuleResponse;
import com.emilybowe.cloudalertsplatform.web.dto.CreateAlertRuleRequest;
import com.emilybowe.cloudalertsplatform.web.dto.UpdateAlertRuleRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rules")
public class AlertRuleController {

    private final AlertRuleService alertRuleService;

    public AlertRuleController(AlertRuleService alertRuleService) {
        this.alertRuleService = alertRuleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertRuleResponse create(@Valid @RequestBody CreateAlertRuleRequest request) {
        return AlertRuleResponse.from(alertRuleService.create(
                request.name(),
                request.service(),
                request.description(),
                request.severity(),
                request.enabled())
        );
    }

    @PatchMapping("/{id}")
    public AlertRuleResponse update(
            @PathVariable UUID id,
            @RequestBody UpdateAlertRuleRequest request
    ) {
        return AlertRuleResponse.from(alertRuleService.update(id,
                request.description(),
                request.severity(),
                request.enabled())
        );
    }

    @GetMapping("/{id}")
    public AlertRuleResponse getById(@PathVariable UUID id) {
        return AlertRuleResponse.from(alertRuleService.getById(id));
    }

    @GetMapping
    public List<AlertRuleResponse> list() {
        return alertRuleService.list().stream()
                .map(AlertRuleResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        alertRuleService.delete(id);
    }

}
