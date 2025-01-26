package com.pjhdev.movieposter.controller;

import com.pjhdev.movieposter.dto.Poster;
import com.pjhdev.movieposter.dto.PosterDetail;
import com.pjhdev.movieposter.dto.RequestPoster;
import com.pjhdev.movieposter.entity.PosterDetailEntity;
import com.pjhdev.movieposter.entity.PosterEntity;
import com.pjhdev.movieposter.service.PosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Entity
// user 1:N poster 1:N poster Detail
// poster 1: 1 file (Direction poster -> file) reverse ban
// poster Detail 1: 1 file (Direction poster detail -> file) reverse ban
// poster post 시 반드시 file 1개 존재 (이미지 파일)
// poster detail post 시 반드시 file 2개 이상 존재 (이미지, 오디오 파일)
// 일단 쿠키로 등록한다. (auth 할 서비스는 아니고 private 하게 사용할 것임으로)
// @CookieValue(value = "user_id", defaultValue = "default") String userId
@RestController
@RequiredArgsConstructor
public class PosterController {
    private final PosterService posterService;

    @GetMapping("/posters")
    public ResponseEntity<List<Poster>> posters(@CookieValue(value = "userId", defaultValue = "default") String userId) {
        List<PosterEntity> list = posterService.getPosters(userId);
        if (list.isEmpty())
            return ResponseEntity.notFound().build();

        List<Poster> body = list.stream()
                .map(Poster::new)
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/poster/{id}")
    public ResponseEntity<List<PosterDetail>> posters(
            @PathVariable Long id
    ) {
        List<PosterDetailEntity> list = posterService.getPosterDetails(id);
        if (list.isEmpty())
            return ResponseEntity.notFound().build();

        List<PosterDetail> body = list.stream()
                .map(PosterDetail::new)
                .toList();

        return ResponseEntity.ok(body);
    }

    @PostMapping("/poster")
    public void addPoster(
            @ModelAttribute RequestPoster requestPoster,
            @RequestParam("file") MultipartFile file,
            @CookieValue(value = "userId", defaultValue = "default") String userId
    ) {
        posterService.savePoster(requestPoster, file, userId);
    }

    @PostMapping("/posterDetails")
    public void addPosterDetails(
            @RequestParam("posterId") Long posterId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        posterService.savePosterDetails(posterId, files);
    }

    @DeleteMapping("/poster/{id}")
    public void deletePoster(
            @PathVariable Long id
    ) {
        posterService.deletePoster(id);
    }
}
