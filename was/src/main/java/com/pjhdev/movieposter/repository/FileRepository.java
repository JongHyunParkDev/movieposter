package com.pjhdev.movieposter.repository;

import com.pjhdev.movieposter.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
