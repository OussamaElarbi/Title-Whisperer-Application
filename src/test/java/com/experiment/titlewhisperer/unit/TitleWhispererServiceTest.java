package com.experiment.titlewhisperer.unit;

import com.experiment.titlewhisperer.config.WebClientConfig;
import com.experiment.titlewhisperer.converter.TitleWhispererConverter;
import com.experiment.titlewhisperer.model.GeneratedTitlesResponse;
import com.experiment.titlewhisperer.model.GptApiResponse;
import com.experiment.titlewhisperer.properties.GptProperties;
import com.experiment.titlewhisperer.service.GptService;
import com.experiment.titlewhisperer.service.TitleWhispererService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.experiment.titlewhisperer.utility.TestConstants.API_KEY;
import static com.experiment.titlewhisperer.utility.TestConstants.CONTENT;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TitleWhispererServiceTest {
    @Mock
    WebClientConfig webClientConfig;
    @Mock
    GptService gptService;
    @Mock
    GptProperties gptProperties;
    @Mock
    TitleWhispererConverter titleWhispererConverter;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    TitleWhispererService titleWhispererService;

    @Test
    void generateTitles_withValidContent_returnsSuccess() {
        GptApiResponse gptApiResponse = new GptApiResponse();

        Mono<GeneratedTitlesResponse> generatedTitlesResponse = Mono.fromSupplier(() -> GeneratedTitlesResponse.builder()
                .generatedTitles(List.of("Title 1", "Title 2"))
                .build());

        when(gptService.whisperTitles(CONTENT, API_KEY)).thenReturn(Mono.just(ResponseEntity.ok(gptApiResponse)));
        when(titleWhispererConverter.convert(ResponseEntity.ok(gptApiResponse))).thenReturn(generatedTitlesResponse);

        StepVerifier.create(titleWhispererService.whisper(CONTENT, API_KEY))
                .assertNext(titles -> {
                    Assertions.assertEquals(titles.getGeneratedTitles().size(), 2);
                    Assertions.assertEquals(titles.getGeneratedTitles().get(0), "Title 1");
                    Assertions.assertEquals(titles.getGeneratedTitles().get(1), "Title 2");
                })
                .then(() -> {
                    verify(gptService, times(1)).whisperTitles(eq(CONTENT), eq(API_KEY));
                    verify(titleWhispererConverter, times(1)).convert(eq(ResponseEntity.ok(gptApiResponse)));
                })
                .verifyComplete();
    }

    @Test
    void generateTitles_withInvalidJson_returnsException() {
        GptApiResponse gptApiResponse = new GptApiResponse();
        when(gptService.whisperTitles(CONTENT, API_KEY)).thenReturn(Mono.just(ResponseEntity.ok(gptApiResponse)));
        when(titleWhispererConverter.convert(ResponseEntity.ok(gptApiResponse))).thenReturn(Mono.error(new RuntimeException("Invalid Json")));

        StepVerifier.create(titleWhispererService.whisper(CONTENT, API_KEY))
                .expectErrorMatches(error -> error.getMessage().equals("Conversion failed: Invalid Json"))
                .verify();
    }

    @Test
    void generateTitles_withInvalidPlan_returnsBadException() {
        when(gptService.whisperTitles(CONTENT, API_KEY)).thenReturn(Mono.error(new RuntimeException("GPT Service Failed")));

        StepVerifier.create(titleWhispererService.whisper(CONTENT, API_KEY))
                .expectErrorMatches(error -> {
                    System.out.println(error.getMessage());
                    return error.getMessage().equals("Error in GPT Service: GPT Service Failed");
                })
                .verify();
    }
}
