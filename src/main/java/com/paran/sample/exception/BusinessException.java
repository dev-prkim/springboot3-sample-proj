package com.paran.sample.exception;

import com.paran.sample.exception.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    private final String code;

    private final String details;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.details = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String details) {
        super(String.format("%s (reason: %s)", errorCode.getMessage(), details));
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.details = String.format("%s (reason: %s)", errorCode.getMessage(), details);
    }

}