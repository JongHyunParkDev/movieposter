package com.pjhdev.movieposter.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tm_poster")
public class PosterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = 16)
    private String color;
    @Column(nullable = false)
    private String userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poster_id") // 외래 키 설정
    private List<PosterDetailEntity> posterDetailEntities;

    @CreationTimestamp // 시간이 자동 입력
    private LocalDateTime createDatetime;

    @Builder
    public PosterEntity(String name, String color, String userId, FileEntity file) {
        this.name = name;
        this.color = color;
        this.userId = userId;
        this.file = file;
    }
}
