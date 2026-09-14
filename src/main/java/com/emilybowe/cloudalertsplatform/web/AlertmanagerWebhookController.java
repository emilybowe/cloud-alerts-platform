package com.emilybowe.cloudalertsplatform.web;

import com.emilybowe.cloudalertsplatform.service.AlertmanagerWebhookService;
import com.emilybowe.cloudalertsplatform.web.dto.AlertmanagerWebhookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
public class AlertmanagerWebhookController {

    private final AlertmanagerWebhookService alertmanagerWebhookService;

    public AlertmanagerWebhookController(AlertmanagerWebhookService alertmanagerWebhookService) {
        this.alertmanagerWebhookService = alertmanagerWebhookService;
    }

    @PostMapping("/alertmanager")
    @ResponseStatus(HttpStatus.OK)
    public void receive(@RequestBody AlertmanagerWebhookRequest request) {
        alertmanagerWebhookService.handle(request);
    }

}
