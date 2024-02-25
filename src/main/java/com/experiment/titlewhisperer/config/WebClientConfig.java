package com.experiment.titlewhisperer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public interface WebClientConfig {
    WebClient webClient();
}
