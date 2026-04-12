package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.TemplatePageSlotEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TemplatePageSlotRepository extends JpaRepository<@NonNull TemplatePageSlotEntity, @NonNull Long> {
    @Query("""
                SELECT tps
                FROM TemplatePageSlotEntity tps
                WHERE tps.id = :templatePageSlotId
                  AND tps.templatePageEntity.id = :templatePageId
                  AND tps.templatePageEntity.templateEntity.id = :templateId
                  AND tps.isActive = :isActive
                  AND tps.isDeleted = :isDeleted
            """)
    Optional<TemplatePageSlotEntity> findTemplatePageSlotEntityBy(
            Long templateId,
            Long templatePageId,
            Long templatePageSlotId,
            Boolean isActive,
            Boolean isDeleted
    );

    @Query("""
                SELECT tps
                FROM TemplatePageSlotEntity tps
                WHERE tps.templatePageEntity.templateEntity.id = :templateId
                  AND tps.templatePageEntity.id = :templatePageId
                  AND tps.isActive = :isActive
                  AND tps.isDeleted = :isDeleted
            """)
    Page<@NonNull TemplatePageSlotEntity> findTemplatePageSlotEntitiesBy(
            Long templateId,
            Long templatePageId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    boolean existsByTemplatePageEntity_IdAndUiBlockCategoryEntity_IdAndIsActiveAndIsDeleted(
            @NonNull Long templatePageId,
            @NonNull Long uiBlockCategoryId,
            boolean isActive,
            boolean isDeleted
    );
}
