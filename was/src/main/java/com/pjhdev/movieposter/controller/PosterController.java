package com.pjhdev.movieposter.controller;

import com.pjhdev.movieposter.dto.Poster;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// user 1:N poster 1:N poster Detail
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
