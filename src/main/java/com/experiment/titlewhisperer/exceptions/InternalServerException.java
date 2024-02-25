package com.experiment.titlewhisperer.exceptions;

import lombok.Data;

import java.io.Serial;
@Data
public class InternalServerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private String responseBody;

    public InternalServerException(String message, String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }
}

