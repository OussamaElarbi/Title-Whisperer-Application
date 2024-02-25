package com.experiment.titlewhisperer.unit;

import com.experiment.titlewhisperer.converter.TitleWhispererConverter;
import com.experiment.titlewhisperer.exceptions.InternalServerException;
import com.experiment.titlewhisperer.model.GeneratedTitlesResponse;
import com.experiment.titlewhisperer.model.GptApiResponse;
import com.experiment.titlewhisperer.utility.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TitleWhispererConverterTest {
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    TitleWhispererConverter titleWhispererConverter;

    @Test
    void convertTitles_withValidGptResponse_returnsSuccess() throws IOException {
        String gptApiResponseString = IOUtils.resourceToString(TestConstants.GPT_API_RESPONSE_SUCCESS, StandardCharsets.UTF_8);
        GptApiResponse gptApiResponse = new ObjectMapper().readValue(gptApiResponseString, GptApiResponse.class);

        Mockito.when(objectMapper.writeValueAsString(gptApiResponse)).thenReturn(gptApiResponseString);
        Mono<GeneratedTitlesResponse> generatedTitlesResponseMono = titleWhispererConverter.convert(ResponseEntity.ok(gptApiResponse));
        StepVerifier.create(generatedTitlesResponseMono)
                .expectNextMatches(generatedTitlesResponse -> generatedTitlesResponse.getGeneratedTitles().size() == 10 &&
                        generatedTitlesResponse.getGeneratedTitles().stream()
                                .anyMatch(title -> title.equals("Accelerating Change: Key Trends Reshaping the Automotive Industry")) &&
                        generatedTitlesResponse.getGeneratedTitles().stream()
                                .allMatch(Objects::nonNull))
                .verifyComplete();
    }

    @Test
    void convertTitles_withNoChoices_throwsInternalServerException() throws IOException {
        String gptApiResponseString = IOUtils.resourceToString(TestConstants.GPT_API_RESPONSE_MISSING_CHOICE, StandardCharsets.UTF_8);
        GptApiResponse gptApiResponse = new ObjectMapper().readValue(gptApiResponseString, GptApiResponse.class);

        Mockito.when(objectMapper.writeValueAsString(gptApiResponse)).thenReturn(gptApiResponseString);
        Mono<GeneratedTitlesResponse> generatedTitlesResponseMono = titleWhispererConverter.convert(ResponseEntity.ok(gptApiResponse));
        StepVerifier.create(generatedTitlesResponseMono)
                .expectErrorSatisfies(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(InternalServerException.class);
                    Assertions.assertThat(throwable.getMessage()).isEqualTo("No choices returned by the GPT model");
                })
                .verify();
    }

}
