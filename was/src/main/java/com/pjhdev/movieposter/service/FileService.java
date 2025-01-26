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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
                .extension(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .build();
        return fileRepository.save(fe);
    }

    public void upload(MultipartFile file, UUID fileId) {
        try {
            Path path = Paths.get(UPLOAD_PATH + fileId + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new ApiException(ExceptionCodeEnum.FAILED_UPLOAD, file.getName());
        }
    }

    public Resource getFile(String fileName) {
        Path path = Paths.get(UPLOAD_PATH + fileName);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new ApiException(ExceptionCodeEnum.NOT_FOUND);
        }
    }

    public void deleteFiles(List<String> deleteFiles) {
        try {
            for (String deleteFile : deleteFiles) {
                Path path = Paths.get(UPLOAD_PATH + deleteFile);
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new ApiException(ExceptionCodeEnum.FAILED_FILE_DELETE);
        }

    }

    @Value("${app.upload-path}")
    private String UPLOAD_PATH;
}
