package com.experiment.titlewhisperer.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class RequestResponseLoggingFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        log.info("Request: {} {}", request.method(), request.url());

        log.info("Request Headers: {}", request.headers());

        return next.exchange(request)
                .flatMap(response -> {
                    log.info("Response Status Code: {}", response.statusCode());

                    log.info("Response Headers: {}", response.headers().asHttpHeaders());

                    response.body(BodyExtractors.toMono(String.class))
                            .map(body -> {
                                log.info("Response Body: {}", body);
                                return response;
                            });

                    return Mono.just(response);

                });
    }
}

