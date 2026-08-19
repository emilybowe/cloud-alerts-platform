package com.emilybowe.cloudalertsplatform.web;

import com.emilybowe.cloudalertsplatform.TestcontainersConfiguration;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.web.dto.AlertRuleResponse;
import com.emilybowe.cloudalertsplatform.web.dto.CreateAlertRuleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
class AlertRuleControllerTest {

    @Test
    void createThenGet(@Autowired RestTestClient restTestClient) {
        AlertRuleResponse created =
        restTestClient.post()
                .uri("/api/v1/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CreateAlertRuleRequest(
                        "high-error-rate",
                        "cloud-alerts-platform",
                        null,
                        Severity.CRITICAL,
                        true
                ))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AlertRuleResponse.class)
                .returnResult()
                .getResponseBody();

        restTestClient.get()
                .uri("/api/v1/rules/{id}", created.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AlertRuleResponse.class)
                .value(rule -> {
                    assertThat(rule.name()).isEqualTo("high-error-rate");
                    assertThat(rule.severity()).isEqualTo(Severity.CRITICAL);
                });

    }
}