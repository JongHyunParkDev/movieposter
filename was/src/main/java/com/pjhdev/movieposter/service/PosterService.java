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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
    public PosterEntity savePoster(RequestPoster requestPoster, MultipartFile file, String userId) {
        FileEntity fe = fileService.save(file);

        PosterEntity pe = PosterEntity.builder()
                .name(requestPoster.getName())
                .color(requestPoster.getColor())
                .userId(userId)
                .file(fe)
                .build();
        posterRepository.save(pe);

        fileService.upload(file, fe.getId());

        return pe;
    }

    @Transactional
    public void savePosterDetails(Long posterId, List<MultipartFile> files) {
        PosterEntity pe = posterRepository.findById(posterId).orElseThrow(()
                -> new ApiException(ExceptionCodeEnum.NOT_FOUND));

        if (!pe.getPosterDetailEntities().isEmpty())
            throw new ApiException(ExceptionCodeEnum.FAILED_POSTER_DETAIL, "Detail Poster already exists");

        List<PosterDetailEntity> pdeList = files.stream()
                .map(file -> {
                    FileEntity fileEntity = fileService.save(file);
                    PosterDetailEntity posterDetailEntity = new PosterDetailEntity();
                    posterDetailEntity.setPoster(pe);
                    posterDetailEntity.setFile(fileEntity);
                    return posterDetailEntity;
                })
                .collect(Collectors.toList());

        posterDetailRepository.saveAll(pdeList);
        pe.setPosterDetailEntities(pdeList);

//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//
//            }
//        });
        for (int i = 0; i < files.size(); i++) {
            fileService.upload(files.get(i), pdeList.get(i).getFile().getId());
        }
    }

    // 아에 따로 빼야할 수도?

    @Transactional
    public void deletePoster(Long id) {
        List<String> deleteFileNames = new ArrayList<>();
        PosterEntity pe = posterRepository.findById(id).orElseThrow(() -> new ApiException(ExceptionCodeEnum.NOT_FOUND));

        pe.getPosterDetailEntities().forEach(pde -> {
            deleteFileNames.add(pde.getFile().getId() + "." + pde.getFile().getExtension());
        });
        posterRepository.deleteById(id);

        // 삭제 중 에러 나올 수 있음으로
        fileService.deleteFiles(deleteFileNames);
    }
}
