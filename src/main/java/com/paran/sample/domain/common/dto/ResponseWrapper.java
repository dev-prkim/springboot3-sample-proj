package com.paran.sample.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.paran.sample.exception.code.ErrorCode.OK;

public record ResponseWrapper<T>(int status, String code, @JsonProperty("data") T content) {

    public static <T> ResponseWrapper<T> of(T content) {
        return new ResponseWrapper<>(OK.getStatus().value(), OK.getCode(), content);
    }
}