package com.pjhdev.movieposter.util;

import com.pjhdev.movieposter.config.ApiException;
import com.pjhdev.movieposter.config.ExceptionCodeEnum;
import com.pjhdev.movieposter.entity.FileType;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

import java.awt.*;

@UtilityClass
public class FileUtil {
    public static FileType getFileType(String contextType) {
        switch (contextType) {
            case MediaType.IMAGE_PNG_VALUE:
            case MediaType.IMAGE_GIF_VALUE:
            case MediaType.IMAGE_JPEG_VALUE:
                return FileType.IMAGE;
            case "audio/mpeg":
                return FileType.AUDIO;
            default:
                throw new ApiException(ExceptionCodeEnum.NOT_APPLY_FILE);
        }
    }

}
