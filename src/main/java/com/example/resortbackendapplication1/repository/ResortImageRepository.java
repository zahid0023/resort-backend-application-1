package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.ResortImageEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortImageRepository extends JpaRepository<@NonNull ResortImageEntity,@NonNull Long> {

    Optional<ResortImageEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull ResortImageEntity> findAllByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, boolean isActive, boolean isDeleted, Pageable pageable);
}
