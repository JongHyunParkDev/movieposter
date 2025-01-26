package com.pjhdev.movieposter.config;

import com.pjhdev.movieposter.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    public ApiExceptionResponse handleApiException(ApiException e) {
        log.error("ApiExceptionHandler.handleException: ", e);
        return new ApiExceptionResponse(e);
    }

    public static class ApiExceptionResponse {
        public int code;
        public String message;

        public ApiExceptionResponse(ApiException e) {
            if (e == null) {
                code = ExceptionCodeEnum.INTERNAL_ERROR.getCode();
                message = "null exception was passed to ApiExceptionHandler";
            } else {
                code = e.code;
                message = CommonUtil.getExceptionReasons(e);
            }
        }
    }
}

