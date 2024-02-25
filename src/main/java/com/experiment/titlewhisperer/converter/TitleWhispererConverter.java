package com.experiment.titlewhisperer.converter;

import com.experiment.titlewhisperer.exceptions.InternalServerException;
import com.experiment.titlewhisperer.model.GeneratedTitlesResponse;
import com.experiment.titlewhisperer.model.GptApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class to Convert GPT API response to GeneratedTitlesResponse
 */
@Component
@AllArgsConstructor
@Log4j2
public class TitleWhispererConverter {

    private final ObjectMapper objectMapper;

    /**
     * Converts GPT API response to GeneratedTitlesResponse
     *
     * @param gptApiResponse GPT API response
     * @return GeneratedTitlesResponse
     */
    @SneakyThrows(JsonProcessingException.class)
    public Mono<GeneratedTitlesResponse> convert(ResponseEntity<GptApiResponse> gptApiResponseResponseEntity) {
        log.info("Extracting titles from GPT API response");
        GptApiResponse gptApiResponse = gptApiResponseResponseEntity.getBody();
        String gptApiResponseJson = objectMapper.writeValueAsString(gptApiResponse);
        return Mono.fromSupplier(() -> GeneratedTitlesResponse.builder()
                .generatedTitles(extractTitles(Arrays.stream(Objects.requireNonNull(gptApiResponse).getChoices())
                        .findFirst()
                        .orElseThrow(() -> {
                            log.error("No choices returned by the GPT model");
                            return new InternalServerException("No choices returned by the GPT model", gptApiResponseJson);
                        })
                        .getMessage()
                        .getContent()))
                .build());
    }

    private List<String> extractTitles(String choice) {
        return Arrays.stream(choice.replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .split(",\\n"))
                .map(String::trim)
                .toList();
    }
}
