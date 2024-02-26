package com.experiment.titlewhisperer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TitleWhispererApplication {

    public static void main(String[] args) {
        SpringApplication.run(TitleWhispererApplication.class, args);
    }

}
