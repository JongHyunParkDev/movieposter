package com.pjhdev.movieposter.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtil {
    public static String getExceptionReasons(Throwable e) {
        if (e == null)
            return "";

        StringBuilder sb = new StringBuilder();
        Throwable cause = null;
        Throwable result = e;

        sb.append(e.getMessage());
        int cnt = 0;
        while (null != (cause = result.getCause()) && (result != cause)) {
            sb.append(" (").append(cause.getMessage());
            result = cause;
            cnt++;
        }

        for (int i = 0; i < cnt; i++) {
            sb.append(")");
        }

        return sb.toString();
    }
}