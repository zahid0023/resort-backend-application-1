package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.TemplatePageSlotVariantEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TemplatePageSlotVariantRepository extends JpaRepository<@NonNull TemplatePageSlotVariantEntity, @NonNull Long> {

    @Query("""
                SELECT tpsv
                FROM TemplatePageSlotVariantEntity tpsv
                WHERE tpsv.id = :id
                  AND tpsv.templatePageSlotEntity.id = :templatePageSlotId
                  AND tpsv.templatePageSlotEntity.templatePageEntity.id = :templatePageId
                  AND tpsv.templatePageSlotEntity.templatePageEntity.templateEntity.id = :templateId
                  AND tpsv.isActive = :isActive
                  AND tpsv.isDeleted = :isDeleted
            """)
    Optional<TemplatePageSlotVariantEntity> findTemplatePageSlotVariantEntityBy(
            Long templateId,
            Long templatePageId,
            Long templatePageSlotId,
            Long id,
            boolean isActive,
            boolean isDeleted
    );

    @Query("""
                SELECT tpsv
                FROM TemplatePageSlotVariantEntity tpsv
                WHERE tpsv.templatePageSlotEntity.id = :templatePageSlotId
                  AND tpsv.templatePageSlotEntity.templatePageEntity.id = :templatePageId
                  AND tpsv.templatePageSlotEntity.templatePageEntity.templateEntity.id = :templateId
                  AND tpsv.isActive = :isActive
                  AND tpsv.isDeleted = :isDeleted
            """)
    Page<@NonNull TemplatePageSlotVariantEntity> findTemplatePageSlotVariantEntitiesBy(
            Long templateId,
            Long templatePageId,
            Long templatePageSlotId,
            boolean isActive,
            boolean isDeleted,
            Pageable pageable
    );
}
