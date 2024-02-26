package com.experiment.titlewhisperer.exceptions;

import com.experiment.titlewhisperer.model.CommonError;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CommonError> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(CommonError.builder()
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .details(Collections.singletonList(ex.getResponseBody()))
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<CommonError> handleTooManyRequestsException(TooManyRequestsException ex) {
        return new ResponseEntity<>(CommonError.builder()
                .errorCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .message(ex.getMessage())
                .details(Collections.singletonList(ex.getResponseBody()))
                .build(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InternalServerException.class})
    public ResponseEntity<CommonError> handleInternalExceptions(InternalServerException ex) {
        return new ResponseEntity<>(CommonError.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .details(Collections.singletonList(ex.getResponseBody()))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({ServiceUnavailableException.class})
    public ResponseEntity<CommonError> handleInternalExceptions(ServiceUnavailableException ex) {
        return new ResponseEntity<>(CommonError.builder()
                .errorCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message(ex.getMessage())
                .details(Collections.singletonList(ex.getResponseBody()))
                .build(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({WebExchangeBindException.class})
    public ResponseEntity<CommonError> handleInternalExceptions(WebExchangeBindException ex) {
        int status = ex.getStatusCode().value();
        return new ResponseEntity<>(CommonError.builder()
                .errorCode(status)
                .message(ex.getBody().getDetail())
                .details(getErrorDetails(ex.getFieldErrors()))
                .build(), HttpStatusCode.valueOf(status));
    }

    private List<String> getErrorDetails(List<FieldError> fieldErrors) {
        List<String> details = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            details.add(fieldError.getField() + " : " + fieldError.getDefaultMessage());
        }
        return details;
    }


}
