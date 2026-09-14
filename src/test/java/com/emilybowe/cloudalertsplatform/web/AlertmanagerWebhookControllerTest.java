package com.emilybowe.cloudalertsplatform.web;

import com.emilybowe.cloudalertsplatform.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import org.springframework.http.MediaType;
import com.emilybowe.cloudalertsplatform.web.dto.IncidentResponse;
import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
class AlertmanagerWebhookControllerTest {

    @Test
    void firingCreatesIncidentThenResolvedClosesIt(@Autowired RestTestClient restTestClient) throws Exception {
        String firing = new String(
                getClass().getResourceAsStream("/alertmanager-firing.json").readAllBytes());
        restTestClient.post()
                .uri("/api/v1/webhooks/alertmanager")
                .contentType(MediaType.APPLICATION_JSON)
                .body(firing)
                .exchange()
                .expectStatus().isOk();
        // second firing must not duplicate
        restTestClient.post()
                .uri("/api/v1/webhooks/alertmanager")
                .contentType(MediaType.APPLICATION_JSON)
                .body(firing)
                .exchange()
                .expectStatus().isOk();
        List<IncidentResponse> afterFiring = restTestClient.get()
                .uri("/api/v1/incidents")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<IncidentResponse>>() {})
                .returnResult()
                .getResponseBody();
        assertThat(afterFiring)
                .filteredOn(i -> "HighErrorRate".equals(i.alertName()))
                .hasSize(1)
                .first()
                .extracting(IncidentResponse::status)
                .isEqualTo(IncidentStatus.OPEN);
        String resolved = new String(
                getClass().getResourceAsStream("/alertmanager-resolved.json").readAllBytes());
        restTestClient.post()
                .uri("/api/v1/webhooks/alertmanager")
                .contentType(MediaType.APPLICATION_JSON)
                .body(resolved)
                .exchange()
                .expectStatus().isOk();
        restTestClient.get()
                .uri("/api/v1/incidents")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<IncidentResponse>>() {})
                .value(list -> assertThat(list)
                        .filteredOn(i -> "HighErrorRate".equals(i.alertName()))
                        .first()
                        .extracting(IncidentResponse::status)
                        .isEqualTo(IncidentStatus.RESOLVED));
    }
}
