package com.experiment.titlewhisperer.model;

import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

@Data
public class GptApiResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private Choice[] choices;
    private Usage usage;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GptApiResponse that = (GptApiResponse) o;
        return Objects.equals(id, that.id) &&
                Arrays.equals(choices, that.choices) &&
                Objects.equals(usage, that.usage);
    }
}
