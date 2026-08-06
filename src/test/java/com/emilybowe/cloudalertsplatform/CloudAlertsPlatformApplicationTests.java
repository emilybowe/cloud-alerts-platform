package com.emilybowe.cloudalertsplatform;

import com.emilybowe.cloudalertsplatform.domain.AlertRule;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.repository.AlertRuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CloudAlertsPlatformApplicationTests {

    @Autowired
    AlertRuleRepository alertRuleRepository;

    @Test
    void save() {
        AlertRule rule = new AlertRule("high-error-rate", "cloud-alerts-platform", Severity.CRITICAL);

        AlertRule savedRule = alertRuleRepository.save(rule);

        assertThat(alertRuleRepository.findById(savedRule.getId()))
                .isPresent()
                .get()
                .extracting(AlertRule::getSeverity, AlertRule::getName)
                .containsExactly(Severity.CRITICAL, "high-error-rate");
    }

}
