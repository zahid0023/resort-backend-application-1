package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortImageEntity;
import com.example.resortbackendapplication1.resort.model.projection.ResortImageSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortImageRepository extends JpaRepository<@NonNull ResortImageEntity, @NonNull Long> {
    Optional<ResortImageEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);
    Page<ResortImageSummary> findAllByResortEntity_IdAndIsActiveAndIsDeleted(Long resortId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
