package com.pjhdev.movieposter.service;

import com.pjhdev.movieposter.config.ApiException;
import com.pjhdev.movieposter.config.ExceptionCodeEnum;
import com.pjhdev.movieposter.entity.FileEntity;
import com.pjhdev.movieposter.repository.FileRepository;
import com.pjhdev.movieposter.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public FileEntity save(MultipartFile file) {
        FileEntity fe = FileEntity.builder()
                .size(file.getSize())
                .type(FileUtil.getFileType(file.getContentType()))
                .contentType(file.getContentType())
                .build();
        return fileRepository.save(fe);
    }

    public void upload(MultipartFile file) {
        try {
            Path path = Paths.get(UPLOAD_PATH + UUID.randomUUID());
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new ApiException(ExceptionCodeEnum.FAILED_UPLOAD, file.getName());
        }
    }

    public Resource getFile(UUID id) {
        Path path = Paths.get(UPLOAD_PATH + id);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new ApiException(ExceptionCodeEnum.NOT_FOUND);
        }
    }

    @Value("app.upload-path")
    private String UPLOAD_PATH;
}
