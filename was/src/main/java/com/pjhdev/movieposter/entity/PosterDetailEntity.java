package com.pjhdev.movieposter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tm_poster_detail")
public class PosterDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poster_id")
    private PosterEntity poster;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileEntity file;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosterDetailEntity that = (PosterDetailEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(poster, that.poster) && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, poster, file);
    }
}
