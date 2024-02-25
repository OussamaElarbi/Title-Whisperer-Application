package com.experiment.titlewhisperer.filter;

import com.experiment.titlewhisperer.exceptions.InternalServerException;
import com.experiment.titlewhisperer.exceptions.ServiceUnavailableException;
import com.experiment.titlewhisperer.exceptions.TooManyRequestsException;
import com.experiment.titlewhisperer.exceptions.UnauthorizedException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.experiment.titlewhisperer.exceptions.constants.ExceptionConstants.*;

@AllArgsConstructor
@Component
public class ClientResponseProcessorFilter {
    private ObjectMapper objectMapper;

    /**
     * Filters client response and throws exception if response status is not 200.
     *
     * @param response client response
     * @return client response
     */
    public Mono<ClientResponse> filterClientResponse(ClientResponse response) {

        HttpStatusCode status = response.statusCode();

        if (HttpStatus.UNAUTHORIZED.equals(status)) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new UnauthorizedException(UNAUTHORIZED_EXCEPTION_MESSAGE, extractErrorMessage(body))));
        }

        if (HttpStatus.TOO_MANY_REQUESTS.equals(status)) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new TooManyRequestsException(TOO_MANY_REQUESTS_EXCEPTION_MESSAGE, extractErrorMessage(body))));
        }

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new InternalServerException(INTERNAL_SERVER_ERROR_EXCEPTION_MESSAGE, extractErrorMessage(body))));
        }

        if (HttpStatus.SERVICE_UNAVAILABLE.equals(status)) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ServiceUnavailableException(SERVICE_UNAVAILABLE_EXCEPTION_MESSAGE, extractErrorMessage(body))));
        }

        return Mono.just(response);
    }

    /**
     * Extracts error message from response body.
     *
     * @param responseBody response body
     * @return error message
     */
    @SneakyThrows
    private String extractErrorMessage(String responseBody) {
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return Optional.ofNullable(jsonNode.get("error"))
                .map(node -> node.get("message"))
                .map(JsonNode::toString)
                .map(message -> message.replaceAll("\"", ""))
                .orElseThrow(RuntimeException::new);
    }

}
