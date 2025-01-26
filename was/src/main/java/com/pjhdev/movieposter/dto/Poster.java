package com.pjhdev.movieposter.dto;

import com.pjhdev.movieposter.entity.PosterEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Poster {
    long id;
    String fileName;
    String name;
    String color;
    LocalDateTime createdDatetime;

    public Poster(PosterEntity pe) {
        this.id = pe.getId();
        this.fileName = pe.getFile().getId() + "." + pe.getFile().getExtension();
        this.name = pe.getName();
        this.color = pe.getColor();
        this.createdDatetime = pe.getCreateDatetime();
    }
}
