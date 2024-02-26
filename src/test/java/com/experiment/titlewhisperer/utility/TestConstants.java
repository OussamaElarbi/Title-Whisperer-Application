package com.experiment.titlewhisperer.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {
    public static final String GPT_API_KEY_HEADER = "sk-ABkz5bqeQEye0a8k7uouWT3ZlbkFJbAsRtXiR7EYcR8dQ96pW";
    public static final String GPT_EXPIRED_API_KEY_HEADER = "sk-ZBkz5bqeQEye0a8k7uouWT3ZlbkFJbAsRtXiR7EYcR8dQ96pW";
    public static final String GPT_INVALID_PLAN_API_KEY_HEADER = "sk-CBkz5bqeQEye0a8k7zoxWT3ZlbkFJbAsRtXiR7EYcR8dQ96pW";
    public static final String GPT_SERVICE_UNAVAILABLE_API_KEY_HEADER = "sk-CBkz5bqeQEye0a8e7uopWT3ZlbkFJbAsRtXiR7EYcR8dQ96pW";
    public static final String GPT_INTERNAL_SERVICE_ERROR_API_KEY_HEADER = "sk-CBkz5bqeQEye0a8k1molWT3ZlbkFJbAsRtXiR7EYcR8dQ96pW";
    public static final String GPT_SERVICE_MISSING_CHOICE_API_KEY_HEADER = "sk-CBkz5bqeQEye0a8k4uouQT3ZlbkFJbAsRtXiR7EYcR8dQ96pW";


    public static final String GPT_API_URL = "/chat/completions";
    public static final String GPT_API_REQUEST_SUCCESS = "/data/gpt/request/gpt-api-request-success.json";
    public static final String GPT_API_RESPONSE_SUCCESS = "/data/gpt/response/gpt-api-response-success.json";

    public static final String GPT_API_RESPONSE_INVALID = "/data/gpt/response/gpt-api-response-invalid.json";

    // GPT Errors
    public static final String GPT_API_RESPONSE_UNAUTHORIZED = "/data/gpt/response/gpt-api-response-unauthorized.json";
    public static final String GPT_API_RESPONSE_TOO_MANY_REQUESTS = "/data/gpt/response/gpt-api-response-too-many-requests.json";
    public static final String GPT_API_RESPONSE_SERVICE_UNAVAILABLE = "/data/gpt/response/gpt-api-response-service-unavailable.json";
    public static final String GPT_API_RESPONSE_SERVICE_ERROR = "/data/gpt/response/gpt-api-response-service-error.json";
    public static final String GPT_API_RESPONSE_MISSING_CHOICE = "/data/gpt/response/gpt-api-response-missing-choice.json";


    public static final String POST_API_REQUEST_SUCCESS = "/data/api/request/post-api-request-success.json";

    public static final String POST_API_REQUEST_EXCEEDING_LENGTH = "/data/api/request/post-api-request-exceeding-length.json";
    public static final String POST_API_REQUEST_EMPTY = "/data/api/request/post-api-request-empty.json";
    public static final String POST_API_RESPONSE_SUCCESS = "/data/api/response/post-api-response-success.json";
    public static final String POST_API_RESPONSE_ERROR_INVALID_CONTENT = "/data/api/response/post-api-response-error-invalid-content.json";

    public static final String POST_API_RESPONSE_ERROR_EMPTY_BODY = "/data/api/response/post-api-response-error-empty-body.json";

    public static final String POST_API_RESPONSE_ERROR_INVALID_API_KEY = "/data/api/response/post-api-response-error-invalid-api-key.json";

    public static final String POST_API_RESPONSE_ERROR_EXPIRED_API_KEY = "/data/api/response/post-api-response-error-expired-api-key.json";

    public static final String POST_API_RESPONSE_ERROR_TOO_MANY_REQUESTS = "/data/api/response/post-api-response-error-too-many-requests.json";

    public static final String POST_API_RESPONSE_ERROR_INVALID_SERVICE = "/data/api/response/post-api-response-error-service-unavailable.json";

    public static final String POST_API_RESPONSE_UNEXPECTED_ERROR = "/data/api/response/post-api-response-error-unexpected-service.json";

    public static final String POST_API_RESPONSE_ERROR_MISSING_CHOICE = "/data/api/response/post-api-response-error-missing-choice.json";

    public static final String CONTENT = "This is a test content";
    public static final String API_KEY = "validApiKey";

}
