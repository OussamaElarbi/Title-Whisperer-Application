package com.experiment.titlewhisperer.service;

import com.experiment.titlewhisperer.config.WebClientConfig;
import com.experiment.titlewhisperer.model.GptApiRequest;
import com.experiment.titlewhisperer.model.GptApiResponse;
import com.experiment.titlewhisperer.properties.GptProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Log4j2
public class GptService {
    private final WebClientConfig webClientConfig;
    private final GptProperties gptProperties;

    public Mono<ResponseEntity<GptApiResponse>> whisperTitles(String content, String apiKey) {
        log.info("Calling ChatGPT API to generate titles");
        GptApiRequest gptApiRequest = gptApiRequest(content);
        return webClientConfig.webClient()
                .post()
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(gptApiRequest)
                .retrieve()
                .toEntity(GptApiResponse.class);
    }


    private GptApiRequest gptApiRequest(String content) {
        return GptApiRequest.builder()
                .model(gptProperties.model())
                .messages(messageList(content))
                .build();
    }

    private List<GptApiRequest.Message> messageList(String content) {
        List<GptApiRequest.Message> messageList = new ArrayList<>();
        messageList.add(GptApiRequest.Message.builder().role(gptProperties.role()).content(gptProperties.description()).build());
        messageList.add(GptApiRequest.Message.builder().role("user").content("Slide Content: " .concat(content)).build());
        return messageList.stream().toList();
    }
}
