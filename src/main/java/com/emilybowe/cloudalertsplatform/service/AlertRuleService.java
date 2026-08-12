package com.emilybowe.cloudalertsplatform.service;

import com.emilybowe.cloudalertsplatform.domain.AlertRule;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.AlertRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AlertRuleService {

    private final AlertRuleRepository alertRuleRepository;

    public AlertRuleService(AlertRuleRepository alertRuleRepository) {
        this.alertRuleRepository = alertRuleRepository;
    }

    public AlertRule create(String name, String service, String description, Severity severity, Boolean enabled) {
        AlertRule rule = new AlertRule(name, service, severity);
        if (description != null) rule.updateDescription(description);
        if (enabled != null) rule.updateEnabled(enabled);
        alertRuleRepository.save(rule);
        return rule;
    }

    public AlertRule getById(UUID id) {
        return alertRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alert rule not found for ID: " + id));
    }

    public List<AlertRule> list() {
        return alertRuleRepository.findAll();
    }

    public AlertRule update(UUID id, String description, Severity severity, Boolean enabled) {
        AlertRule rule = this.getById(id);
        if (description != null) rule.updateDescription(description);
        if (severity != null) rule.updateSeverity(severity);
        if (enabled != null) rule.updateEnabled(enabled);
        alertRuleRepository.save(rule);
        return rule;
    }

    public void delete(UUID id) {
        AlertRule rule = this.getById(id);
        alertRuleRepository.delete(rule);
    }
}
