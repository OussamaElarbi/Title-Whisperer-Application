package com.experiment.titlewhisperer.exceptions.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConstants {

    public static final String UNAUTHORIZED_EXCEPTION_MESSAGE = "Unauthorized request";
    public static final String NO_CHOICE_FOUND_EXCEPTION_MESSAGE = "No choice found";
    public static final String SERVICE_UNAVAILABLE_EXCEPTION_MESSAGE = "Servers are experiencing high traffic.";
    public static final String INTERNAL_SERVER_ERROR_EXCEPTION_MESSAGE = "Internal server error";
    public static final String BAD_REQUEST_EXCEPTION_MESSAGE = "Bad request";

    public static final String TOO_MANY_REQUESTS_EXCEPTION_MESSAGE = "Rate limit reached for requests";
}
