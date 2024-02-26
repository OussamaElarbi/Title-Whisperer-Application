package com.experiment.titlewhisperer.exceptions;

import lombok.Data;

import java.io.Serial;

@Data
public class TooManyRequestsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private String responseBody;

    public TooManyRequestsException(String message, String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }

}

