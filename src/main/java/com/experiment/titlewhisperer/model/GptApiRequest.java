package com.experiment.titlewhisperer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("com.experiment.titlewhisperer.gpt")
@Builder
@Data
public class GptApiRequest {
    private String model;
    private List<Message> messages;

    @Builder
    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
