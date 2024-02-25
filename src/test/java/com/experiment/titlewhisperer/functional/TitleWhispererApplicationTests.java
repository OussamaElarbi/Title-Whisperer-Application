package com.experiment.titlewhisperer.functional;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.experiment.titlewhisperer.utility.TestConstants.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "com.experiment.titlewhisperer.client.service-uri=http://localhost:${wiremock.server.port}/chat/completions"
        })
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class TitleWhispererApplicationTests {
    @Autowired
    WebTestClient webTestClient;

    @BeforeAll
    static void setUp() throws IOException {
        stub(GPT_API_KEY_HEADER, GPT_API_RESPONSE_SUCCESS, 200);
        stub(GPT_SERVICE_MISSING_CHOICE_API_KEY_HEADER, GPT_API_RESPONSE_MISSING_CHOICE, 200);
        stub(GPT_EXPIRED_API_KEY_HEADER, GPT_API_RESPONSE_UNAUTHORIZED, 401);
        stub(GPT_INVALID_PLAN_API_KEY_HEADER, GPT_API_RESPONSE_TOO_MANY_REQUESTS, 429);
        stub(GPT_SERVICE_UNAVAILABLE_API_KEY_HEADER, GPT_API_RESPONSE_SERVICE_UNAVAILABLE, 503);
        stub(GPT_INTERNAL_SERVICE_ERROR_API_KEY_HEADER, GPT_API_RESPONSE_SERVICE_ERROR, 500);
    }

    @Test
    void generateTitles_withValidContent_returnsSuccess() throws IOException {
        post(POST_API_REQUEST_SUCCESS, GPT_API_KEY_HEADER)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_SUCCESS, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withBodyExceedingMinimumLength_throwsBadRequest() throws IOException {
        post(POST_API_REQUEST_EXCEEDING_LENGTH, GPT_API_KEY_HEADER)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_INVALID_CONTENT, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withEmptyBody_throwsBadRequest() throws IOException {
        post(POST_API_REQUEST_EMPTY, GPT_API_KEY_HEADER)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_EMPTY_BODY, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withEmptyApiKey_throwsUnauthorizedError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, null)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_INVALID_API_KEY, StandardCharsets.UTF_8), true);
    }


    @Test
    void generateTitles_withInvalidApiKeyFormat_throwsUnauthorizedError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, "sk!-H?H-")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_INVALID_API_KEY, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withExpiredApiKey_throwsUnauthorizedError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, GPT_EXPIRED_API_KEY_HEADER)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_EXPIRED_API_KEY, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withInvalidPlan_throwsTooManyRequestsError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, GPT_INVALID_PLAN_API_KEY_HEADER)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_TOO_MANY_REQUESTS, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withInvalidService_throwsServiceUnavailableError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, GPT_SERVICE_UNAVAILABLE_API_KEY_HEADER)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_INVALID_SERVICE, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withMissingChoice_throwsInternalServiceError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, GPT_SERVICE_MISSING_CHOICE_API_KEY_HEADER)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_ERROR_MISSING_CHOICE, StandardCharsets.UTF_8), true);
    }

    @Test
    void generateTitles_withUnexpectedResponse_throwsInternalServiceError() throws IOException {
        post(POST_API_REQUEST_SUCCESS, GPT_INTERNAL_SERVICE_ERROR_API_KEY_HEADER)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .json(IOUtils.resourceToString(POST_API_RESPONSE_UNEXPECTED_ERROR, StandardCharsets.UTF_8), true);
    }

    WebTestClient.RequestHeadersSpec<?> post(String requestJsonPath, String apiKey) throws IOException {
        return webTestClient
                .post()
                .uri("/titles/generate")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Gpt-Api-Key", apiKey)
                .bodyValue(IOUtils.resourceToString(requestJsonPath, StandardCharsets.UTF_8));
    }

    static void stub(String apiKey, String responsePath, int status) throws IOException {
        stubFor(WireMock.post(urlEqualTo(GPT_API_URL))
                .withHeader("Authorization", new EqualToPattern("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(IOUtils.resourceToString(responsePath, StandardCharsets.UTF_8))));
    }

}
