package com.paran.sample.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ApiLoggingData(
        @JsonProperty(value = "traceId") String traceId,
        @JsonProperty(value = "class") String className,
        @JsonProperty(value = "http_method") String httpMethod,
        @JsonProperty(value = "uri") String uri,
        @JsonProperty(value = "method") String method,
        @JsonProperty(value = "params") Map<String, String> params,
        @JsonProperty(value = "log_time") String logTime,
        @JsonProperty(value = "server_ip") String serverIp,
        @JsonProperty(value = "device_type") String deviceType,
        @JsonProperty(value = "request_body") Object requestBody,
        @JsonProperty(value = "response_body") Object responseBody,
        @JsonProperty(value = "elapsed_time") String elapsedTime
) {
    public ApiLoggingData withResponseBody(Object value) {
        return new ApiLoggingData(traceId, className, httpMethod, uri, method, params, logTime, serverIp, deviceType, requestBody, value, elapsedTime);
    }

    public ApiLoggingData withElapsedTime(String value) {
        return new ApiLoggingData(traceId, className, httpMethod, uri, method, params, logTime, serverIp, deviceType, requestBody, responseBody, value);
    }
}
