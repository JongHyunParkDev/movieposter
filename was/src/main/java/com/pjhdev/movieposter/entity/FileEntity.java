package com.pjhdev.movieposter.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tm_file")
public class FileEntity {
    @Id
    private UUID id;
    private FileType type; // image or audio
    private String contentType;
    private long size;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID(); // 엔티티가 저장되기 전에 UUID 생성
    }

    @Builder
    public FileEntity(FileType type, String contentType, long size) {
        this.type = type;
        this.contentType = contentType;
        this.size = size;
    }
}
