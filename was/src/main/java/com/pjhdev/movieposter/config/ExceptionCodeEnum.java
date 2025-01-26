package com.pjhdev.movieposter.config;

import lombok.Getter;

@Getter
public enum ExceptionCodeEnum {
    GENERIC(1000, "ERROR"),
    NOT_FOUND(1001, "NOT FOUND"),
    INTERNAL_ERROR(1002, "INTERNAL ERROR"),

    INVALID_REQUEST(1100, "INVALID REQUEST"),
    INVALID_ARGUMENT(1101, "INVALID ARGUMENT"),

    FAILED_UPLOAD(1200, "FAILED UPLOAD"),
    NOT_APPLY_FILE(1201, "NOT APPLY FILE"),
    FAILED_FILE_DELETE(1202, "FAILED FILE DELETE"),
    FAILED_POSTER_DETAIL(1203, "FAILED POSTER DETAIL");

    private final int code;
    private final String message;

    ExceptionCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}