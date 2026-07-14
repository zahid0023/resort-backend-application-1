package com.example.resortbackendapplication1.imagehosting.repository;

import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.model.projection.ResortImageHostingConfigSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortImageHostingConfigRepository
        extends JpaRepository<@NonNull ResortImageHostingConfigEntity, @NonNull Long> {

    Optional<ResortImageHostingConfigEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    Page<ResortImageHostingConfigSummary> findAllByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
