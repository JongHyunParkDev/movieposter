package com.pjhdev.movieposter.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Poster {
    long id;
    String url;
    String color;
    String name;
    LocalDate createdDate;
}
