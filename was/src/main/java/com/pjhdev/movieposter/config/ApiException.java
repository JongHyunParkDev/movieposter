package com.pjhdev.movieposter.config;

public class ApiException extends RuntimeException {
    public int code;

    public ApiException(ExceptionCodeEnum exceptionCodeEnum) {
        super(exceptionCodeEnum.getMessage());
        this.code = exceptionCodeEnum.getCode();
    }

    public ApiException(ExceptionCodeEnum exceptionCodeEnum, String addMsg) {
        super(exceptionCodeEnum.getMessage() + " " + addMsg);
        this.code = exceptionCodeEnum.getCode();
    }
}
