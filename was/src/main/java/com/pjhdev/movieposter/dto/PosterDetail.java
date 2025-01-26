package com.pjhdev.movieposter.dto;

import com.pjhdev.movieposter.entity.FileType;
import com.pjhdev.movieposter.entity.PosterDetailEntity;
import com.pjhdev.movieposter.entity.PosterEntity;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PosterDetail {
    UUID fileName;
    FileType fileType;

    public PosterDetail(PosterDetailEntity pde) {
        this.fileName = pde.getFile().getId();
        this.fileType = pde.getFile().getType();
    }
}
