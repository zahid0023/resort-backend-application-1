package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.projection.RoomCategorySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomCategoryRepository extends JpaRepository<@NonNull RoomCategoryEntity, @NonNull Long> {

    Optional<RoomCategoryEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull RoomCategorySummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);
}
