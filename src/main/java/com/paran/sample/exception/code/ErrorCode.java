package com.paran.sample.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    // [Success] 0000 ~ 0999
    OK(HttpStatus.OK, "0000", "Ok"),

    // [Input Error] 1000 ~ 1999
    NOT_FOUND(HttpStatus.NOT_FOUND, "1000", "Not found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"1001", "Bad request"),

    //

    // [System Error] 9000 ~ 9999
    NOT_DEFINED_ERR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "An error occurred during the service");

    private final HttpStatus status;
    private final String code;
    private final String message;
}