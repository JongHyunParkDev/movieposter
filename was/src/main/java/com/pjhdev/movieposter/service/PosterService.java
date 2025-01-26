package com.pjhdev.movieposter.service;

import com.pjhdev.movieposter.config.ApiException;
import com.pjhdev.movieposter.config.ExceptionCodeEnum;
import com.pjhdev.movieposter.dto.RequestPoster;
import com.pjhdev.movieposter.entity.FileEntity;
import com.pjhdev.movieposter.entity.FileType;
import com.pjhdev.movieposter.entity.PosterDetailEntity;
import com.pjhdev.movieposter.entity.PosterEntity;
import com.pjhdev.movieposter.repository.FileRepository;
import com.pjhdev.movieposter.repository.PosterDetailRepository;
import com.pjhdev.movieposter.repository.PosterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosterService {
    private final PosterDetailRepository posterDetailRepository;
    private final PosterRepository posterRepository;
    private final FileService fileService;

    public List<PosterEntity> getPosters(String userId) {
        return posterRepository.findAllByUserId(userId);
    }

    public List<PosterDetailEntity> getPosterDetails(Long id) {
        return posterRepository.findById(id).orElseThrow(()
                -> new ApiException(ExceptionCodeEnum.NOT_FOUND)).getPosterDetailEntities();
    }

    @Transactional
    public void savePoster(RequestPoster requestPoster, MultipartFile file, String userId) {
        FileEntity fe = fileService.save(file);

        PosterEntity pe = PosterEntity.builder()
                .name(requestPoster.getName())
                .color(requestPoster.getColor())
                .userId(userId)
                .file(fe)
                .build();
        posterRepository.save(pe);

        fileService.upload(file);
    }

    @Transactional
    public void savePosterDetails(Long posterId, List<MultipartFile> files) {
        PosterEntity pe = posterRepository.findById(posterId).orElseThrow(()
                -> new ApiException(ExceptionCodeEnum.NOT_FOUND));

        List<PosterDetailEntity> pdeList = files.stream()
                .map(file -> {
                    FileEntity fileEntity = fileService.save(file);
                    PosterDetailEntity posterDetailEntity = new PosterDetailEntity();
                    posterDetailEntity.setFile(fileEntity);
                    return posterDetailEntity;
                })
                .collect(Collectors.toList());

        pe.setPosterDetailEntities(pdeList);

        posterDetailRepository.saveAll(pdeList);

        files.forEach(fileService::upload);
    }

    // 아에 따로 빼야할 수도?

    public void deletePoster(Long id) {
        posterRepository.deleteById(id);
    }
}
