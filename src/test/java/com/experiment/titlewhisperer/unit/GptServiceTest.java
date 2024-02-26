package com.experiment.titlewhisperer.unit;

import com.experiment.titlewhisperer.config.WebClientConfig;
import com.experiment.titlewhisperer.model.GptApiResponse;
import com.experiment.titlewhisperer.properties.GptProperties;
import com.experiment.titlewhisperer.service.GptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class GptServiceTest {
    public static MockWebServer mockWebServer;
    @Mock
    WebClientConfig webClientConfig;
    @Mock
    WebClient webClient;
    @Mock
    GptProperties gptProperties;
    @InjectMocks
    GptService gptService;
    String content = "This is a test content";
    String apiKey = "validApiKey";

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void generateTitles_withValidApiKey_returnsSuccess() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        GptApiResponse gptApiResponse = new GptApiResponse();
        gptApiResponse.setId(String.valueOf(1));
        GptApiResponse.Choice choice = new GptApiResponse.Choice();
        choice.setIndex(0);
        GptApiResponse.Choice[] choicesArray = new GptApiResponse.Choice[]{choice};
        gptApiResponse.setChoices(choicesArray);
        gptApiResponse.setUsage(new GptApiResponse.Usage());

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(gptApiResponse))
                .addHeader("Content-Type", "application/json"));

        when(webClientConfig.webClient()).thenReturn(WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .baseUrl(String.format("http://localhost:%s", mockWebServer.getPort()))
                .build());


        StepVerifier.create(gptService.whisperTitles(content, apiKey))
                .consumeNextWith(responseEntity -> {
                    GptApiResponse actualResponse = responseEntity.getBody();
                    assertThat(actualResponse).isEqualTo(gptApiResponse);
                })
                .verifyComplete();

    }


}
