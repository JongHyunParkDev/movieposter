package com.pjhdev.movieposter.repository;

import com.pjhdev.movieposter.entity.PosterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosterRepository extends JpaRepository<PosterEntity, Long> {
    List<PosterEntity> findAllByUserId(String userId);
}
