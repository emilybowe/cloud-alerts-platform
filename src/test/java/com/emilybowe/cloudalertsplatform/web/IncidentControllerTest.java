package com.emilybowe.cloudalertsplatform.web;

import com.emilybowe.cloudalertsplatform.TestcontainersConfiguration;
import com.emilybowe.cloudalertsplatform.domain.IncidentStatus;
import com.emilybowe.cloudalertsplatform.domain.Severity;
import com.emilybowe.cloudalertsplatform.web.dto.CreateIncidentRequest;
import com.emilybowe.cloudalertsplatform.web.dto.IncidentResponse;
import com.emilybowe.cloudalertsplatform.web.dto.UpdateIncidentRequest;
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
class IncidentControllerTest {

    @Autowired
    RestTestClient restTestClient;

    @Test
    void ackThenRejectTransitionBackToOpen() {
        IncidentResponse created = restTestClient.post().uri("/api/v1/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CreateIncidentRequest(
                        "overloaded",
                        Severity.CRITICAL,
                        "service overloaded",
                        null,
                        null
                ))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IncidentResponse.class)
                .returnResult()
                .getResponseBody();

        restTestClient.patch().uri("/api/v1/incidents/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateIncidentRequest(
                        IncidentStatus.ACKNOWLEDGED,
                        null
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(IncidentResponse.class)
                .value( incident -> {
                    assertThat(incident.status()).isEqualTo(IncidentStatus.ACKNOWLEDGED);
                    assertThat(incident.acknowledgedAt()).isNotNull();
                });

        restTestClient.patch().uri("/api/v1/incidents/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateIncidentRequest(
                        IncidentStatus.OPEN,
                        null
                ))
                .exchange()
                .expectStatus().isEqualTo(409);
    }
}