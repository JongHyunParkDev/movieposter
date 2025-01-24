package com.pjhdev.movieposter.controller;

import com.pjhdev.movieposter.dto.Poster;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Entity
// user 1:N poster 1:N poster Detail
// poster 1: 1 file (Direction poster -> file) reverse ban
// poster Detail 1: 1 file (Direction poster detail -> file) reverse ban
// poster post 시 반드시 file 1개 존재 (이미지 파일)
// poster detail post 시 반드시 file 2개 이상 존재 (이미지, 오디오 파일)

@RestController
@RequiredArgsConstructor
public class PosterController {

    @GetMapping("/posters")
    public ResponseEntity<List<Poster>> posters() {

        List<Poster> posters = new ArrayList<>();

        return ResponseEntity
                .ok()
                .body(posters);
    }

    @GetMapping("/poster/{id}")
    public ResponseEntity<Poster> getPosterById(@PathVariable Long id) {

        Optional<Poster> poster = Optional.empty();

        if (poster.isPresent()) {
            return ResponseEntity.ok(poster.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
