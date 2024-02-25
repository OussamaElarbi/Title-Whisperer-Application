package com.experiment.titlewhisperer.unit;

import com.experiment.titlewhisperer.config.WebClientConfig;
import com.experiment.titlewhisperer.model.GptApiRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

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

        GptApiRequest gptApiRequest = GptApiRequest.builder()
                .model(gptProperties.model())
                .messages(List.of(GptApiRequest.Message.builder().content(content).build()))
                .build();

        GptApiResponse gptApiResponse = new GptApiResponse();
        gptApiResponse.setId(String.valueOf(1));
        GptApiResponse.Choice choice = new GptApiResponse.Choice();
        choice.setIndex(0);
        GptApiResponse.Choice[] choicesArray = new GptApiResponse.Choice[]{choice};
        gptApiResponse.setChoices(choicesArray);
        gptApiResponse.setUsage(new GptApiResponse.Usage());

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(gptApiRequest))
                .addHeader("Content-Type", "application/json"));

        when(webClientConfig.webClient()).then(returns -> {
            WebClient.Builder builder = WebClient.builder();
            builder.baseUrl(String.format("http://localhost:%s", mockWebServer.getPort()));
            return builder.build();
        });


        WebClient.ResponseSpec expectedResponse = webClientConfig.webClient()
                .post()
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(gptApiRequest)
                .retrieve()
                .onRawStatus(status -> status == 404, clientResponse -> Mono.error(new Exception("Resource not found")));

        GptApiResponse expectedResponseBody = new GptApiResponse();
        expectedResponseBody.setId(String.valueOf(1));
        expectedResponseBody.setChoices(choicesArray);
        expectedResponseBody.setUsage(new GptApiResponse.Usage());


        StepVerifier.create(gptService.whisperTitles(content, apiKey))
                .expectNext(ResponseEntity.ok(expectedResponseBody))
                .verifyComplete();

    }


}
