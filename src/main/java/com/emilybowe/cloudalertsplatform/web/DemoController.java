package com.emilybowe.cloudalertsplatform.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DemoController {

    @GetMapping("/demo/slow")
    public void getSlow(@RequestParam(defaultValue = "2000") int ms) throws InterruptedException {
        if (ms < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ms must be >= 0");
        }
        Thread.sleep(ms);
    }

    @GetMapping("/demo/error")
    public String getError(@RequestParam(defaultValue = "0.1") double rate) {
        if (rate < 0.0 || rate > 1.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rate must be between 0.0 and 1.0");
        }

        if (Math.random() < rate) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "injected error");
        }

        return "ok";
    }

    @GetMapping("/demo/health")
    public String getHealth() {
        return "ok";
    }

}
