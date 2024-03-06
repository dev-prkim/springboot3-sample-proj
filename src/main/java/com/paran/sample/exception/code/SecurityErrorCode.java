package com.paran.sample.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SecurityErrorCode {

    INVALID_ACCESS(HttpStatus.FORBIDDEN, "INVALID_ACCESS", "This is an invalid access"),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_JWT_TOKEN", "The JWT token has expired"),
    UNAVAILABLE_TOKEN(HttpStatus.UNAUTHORIZED, "UNAVAILABLE_TOKEN", "The JWT token is unavailable. Please login again"),
    UNAVAILABLE_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "UNAVAILABLE_REFRESH_TOKEN", "The refresh token is unavailable. Please login again");

    private final HttpStatus status;
    private final String code;
    private final String message;
}