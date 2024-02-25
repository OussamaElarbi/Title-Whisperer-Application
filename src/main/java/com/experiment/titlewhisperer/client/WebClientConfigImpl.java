package com.experiment.titlewhisperer.client;

import com.experiment.titlewhisperer.config.WebClientConfig;
import com.experiment.titlewhisperer.filter.ClientResponseProcessorFilter;
import com.experiment.titlewhisperer.filter.RequestResponseLoggingFilter;
import com.experiment.titlewhisperer.properties.ClientProperties;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;

@AllArgsConstructor
@Component
public class WebClientConfigImpl implements WebClientConfig {
    private final ClientProperties clientProperties;
    private final RequestResponseLoggingFilter requestResponseLoggingFilter;
    private final ClientResponseProcessorFilter clientResponseValidatorFilter;

    @Override
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .filter(requestResponseLoggingFilter)
                .filter(ExchangeFilterFunction
                        .ofResponseProcessor(clientResponseValidatorFilter::filterClientResponse))
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(clientProperties.serviceUri())
                .build();
    }

    @Bean
    HttpClient httpClient() {
        return HttpClient.create()
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .responseTimeout(Duration.ofMillis(clientProperties.responseTimeout()))
                .doOnRequest((request, connection) -> {
                    connection
                            .addHandlerLast(new ReadTimeoutHandler(clientProperties.readTimeout()))
                            .addHandlerLast(new WriteTimeoutHandler(clientProperties.writeTimeout()));
                });

    }

}
