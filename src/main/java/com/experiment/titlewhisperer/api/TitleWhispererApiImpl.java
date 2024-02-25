package com.experiment.titlewhisperer.api;

import com.experiment.titlewhisperer.model.GeneratedTitlesResponse;
import com.experiment.titlewhisperer.model.TitlesGeneratePostRequest;
import com.experiment.titlewhisperer.service.TitleWhispererService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class TitleWhispererApiImpl implements TitlesApiDelegate {
    private final TitleWhispererService titleWhispererService;

    /**
     *{@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<GeneratedTitlesResponse>> titlesGeneratePost(String gptApiKey, Mono<TitlesGeneratePostRequest> titlesGeneratePostRequest, ServerWebExchange exchange) {
        return titlesGeneratePostRequest.map(TitlesGeneratePostRequest::getContent)
                .flatMap(content -> titleWhispererService.whisper(content, gptApiKey))
                .map(ResponseEntity::ok);
    }

}
