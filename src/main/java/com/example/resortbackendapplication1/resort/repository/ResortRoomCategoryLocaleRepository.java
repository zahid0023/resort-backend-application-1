package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryLocaleRepository extends JpaRepository<@NonNull ResortRoomCategoryLocaleEntity, @NonNull Long> {

    Optional<ResortRoomCategoryLocaleEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortRoomCategoryLocaleEntity> findByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortRoomCategoryLocaleEntity> findByResortRoomCategoryEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
