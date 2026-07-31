package com.emilybowe.cloudalertsplatform;

import org.springframework.boot.SpringApplication;

public class TestCloudAlertsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.from(CloudAlertsPlatformApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
