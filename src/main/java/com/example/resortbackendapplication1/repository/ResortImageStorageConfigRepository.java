package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.ResortImageStorageConfigEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortImageStorageConfigRepository extends JpaRepository<@NonNull ResortImageStorageConfigEntity, @NonNull Long> {

    Optional<ResortImageStorageConfigEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, boolean isActive, boolean isDeleted);

    Optional<ResortImageStorageConfigEntity> findByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, boolean isActive, boolean isDeleted);

    Page<@NonNull ResortImageStorageConfigEntity> findAllByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, boolean isActive, boolean isDeleted, Pageable pageable);
}
