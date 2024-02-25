package com.experiment.titlewhisperer.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.experiment.titlewhisperer.gpt")
public record GptProperties(@NotNull String model, @NotNull String role, @NotNull String description) {

}