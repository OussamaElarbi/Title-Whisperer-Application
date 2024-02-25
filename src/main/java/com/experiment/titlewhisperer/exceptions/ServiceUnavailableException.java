package com.experiment.titlewhisperer.exceptions;

import lombok.Data;

import java.io.Serial;
@Data
public class ServiceUnavailableException extends RuntimeException {

    private String responseBody;

    @Serial
    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException(String message, String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }

}
