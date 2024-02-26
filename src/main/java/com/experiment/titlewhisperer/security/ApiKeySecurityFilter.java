package com.experiment.titlewhisperer.security;

import com.experiment.titlewhisperer.model.CommonError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
public class ApiKeySecurityFilter implements WebFilter {

    @SneakyThrows
    @Override
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getMethod().name().equalsIgnoreCase("POST") && request.getPath().value().equals("/titles/generate")) {

            String apiKey = request.getHeaders().getFirst("Gpt-Api-Key");

            if (StringUtils.isNotBlank(apiKey) & isValidApiKey(apiKey)) {
                chain.filter(exchange);
            } else {
                return handleInvalidApiKeyHeader(exchange);
            }
        }
        return chain.filter(exchange);
    }

    /**
     * This method checks if the API key is valid.
     *
     * @param apiKey The API key
     * @return True if the API key is valid, false otherwise
     */
    private boolean isValidApiKey(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            return false;
        }

        String apiKeyPattern = "^[a-zA-Z0-9-]+$";

        Pattern pattern = Pattern.compile(apiKeyPattern);
        Matcher matcher = pattern.matcher(apiKey);

        return matcher.matches();
    }

    /**
     * This method handles the invalid API key header.
     *
     * @param exchange The server web exchange
     * @return The Mono<Void>
     */
    private Mono<Void> handleInvalidApiKeyHeader(ServerWebExchange exchange) throws IOException {
        log.error("Empty or Invalid API Key");

        CommonError errorResponse = CommonError.builder()
                .message("Unauthorized")
                .details(Collections.singletonList("Error: Invalid API Key, please fetch a valid API Key from https://platform.openai.com/account/api-keys"))
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .build();

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer dataBuffer = exchange.getResponse().bufferFactory()
                .wrap(new ObjectMapper().writeValueAsBytes(errorResponse));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));

    }

}
