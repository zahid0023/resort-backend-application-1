package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortRoomCategoryRepository extends JpaRepository<@NonNull ResortRoomCategoryEntity, @NonNull Long> {

    Optional<ResortRoomCategoryEntity> findByResort_IdAndIdAndIsActiveAndIsDeleted(Long resortId, Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull ResortRoomCategoryEntity> findAllByResort_IdAndIsActiveAndIsDeleted(Long resortId, boolean isActive, boolean isDeleted, Pageable pageable);
}
