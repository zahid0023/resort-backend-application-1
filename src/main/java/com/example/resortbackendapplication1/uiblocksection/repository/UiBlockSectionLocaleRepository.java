package com.example.resortbackendapplication1.uiblocksection.repository;

import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UiBlockSectionLocaleRepository extends JpaRepository<@NonNull UiBlockSectionLocaleEntity, @NonNull Long> {

    Optional<UiBlockSectionLocaleEntity> findByUiBlockSectionEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long uiBlockSectionId, Long id, Boolean isActive, Boolean isDeleted);
}
