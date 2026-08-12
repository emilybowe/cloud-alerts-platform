package com.emilybowe.cloudalertsplatform.service;

import com.emilybowe.cloudalertsplatform.domain.AlertRule;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.AlertRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertRuleServiceTest {

    @Mock
    AlertRuleRepository alertRuleRepository;

    @InjectMocks
    AlertRuleService alertRuleService;

    @Test
    void create() {
        when(alertRuleRepository.save(any(AlertRule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlertRule rule = alertRuleService.create("overloaded", "wallet", null, Severity.WARNING, true);
        assertThat(rule.getName()).isEqualTo("overloaded");
        assertThat(rule.getService()).isEqualTo("wallet");
        assertThat(rule.getDescription()).isNull();
        assertThat(rule.getSeverity()).isEqualTo(Severity.WARNING);
        assertThat(rule.getEnabled()).isTrue();

       verify(alertRuleRepository).save(rule);

    }

}