package com.pjhdev.movieposter.controller;

import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final String UPLOAD_DIR = "src/main/resources/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String uuid = UUID.randomUUID().toString();
            String fileName = file.getName();

            Path path = Paths.get(UPLOAD_DIR + uuid);
            Files.copy(file.getInputStream(), path);

            // DB 적재

            return ResponseEntity
                    .ok()
                    .body("upload success");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("failed upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path path = Paths.get(UPLOAD_DIR + filename);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
