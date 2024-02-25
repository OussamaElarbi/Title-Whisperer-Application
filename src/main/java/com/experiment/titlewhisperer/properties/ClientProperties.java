package com.experiment.titlewhisperer.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("com.experiment.titlewhisperer.client")
public record ClientProperties(@NotNull String serviceUri, long responseTimeout, int readTimeout, int writeTimeout) {
}
