package com.experiment.titlewhisperer.service;

import com.experiment.titlewhisperer.converter.TitleWhispererConverter;
import com.experiment.titlewhisperer.model.GeneratedTitlesResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
@Log4j2
public class TitleWhispererService {
    private final GptService gptService;
    private final TitleWhispererConverter titleWhispererConverter;

    public Mono<GeneratedTitlesResponse> whisper(String content, String apiKey) {
        return gptService.whisperTitles(content, apiKey)
                .onErrorMap(RuntimeException.class, ex -> new RuntimeException("Error in GPT Service: " + ex.getMessage(), ex))
                .flatMap(responseEntity -> titleWhispererConverter.convert(responseEntity)
                        .onErrorMap(ex -> new RuntimeException("Conversion failed: " + ex.getMessage(), ex)));
    }

}
