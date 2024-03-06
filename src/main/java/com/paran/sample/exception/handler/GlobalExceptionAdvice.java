package com.paran.sample.exception.handler;

import com.paran.sample.exception.BusinessException;
import com.paran.sample.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    private final static String ERROR_CODE_PROPERTY_NAME = "code";

    /**
     * BusinessException : 사용자 정의 오류
     */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleException(BusinessException e) {
        log.error("BUSINESS EXCEPTION : {}", e.getDetails());
        var problemDetail = ProblemDetail.forStatusAndDetail(e.getStatus(), e.getDetails());
        problemDetail.setProperty(ERROR_CODE_PROPERTY_NAME, e.getCode());
        return problemDetail;
    }

    /**
     * Recode 내 정의된 NotNull 에러
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ProblemDetail handleException(HttpMessageNotReadableException e) {
        log.error("BAD_REQUEST : {}", e.getCause().toString());

        var err = ErrorCode.BAD_REQUEST;
        var problemDetail = ProblemDetail.forStatusAndDetail(err.getStatus(), err.getMessage());
        problemDetail.setProperty(ERROR_CODE_PROPERTY_NAME, err.getCode());

        if(e.getCause().getCause() != null) {
            problemDetail.setDetail(e.getCause().getCause().getMessage());
        }

        return problemDetail;
    }

    /**
     * Exception : 정의되지 않은 모든 에러
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        log.error("GLOBAL EXCEPTION : {}", e.getMessage());
        var err = ErrorCode.NOT_DEFINED_ERR;
        var problemDetail = ProblemDetail.forStatusAndDetail(err.getStatus(), err.getMessage());
        problemDetail.setProperty(ERROR_CODE_PROPERTY_NAME, err.getCode());
        return problemDetail;
    }

}
