package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.TemplatePageEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplatePageRepository extends JpaRepository<@NonNull TemplatePageEntity, @NonNull Long> {
    Optional<@NonNull TemplatePageEntity> findByTemplateEntity_IdAndIdAndIsActiveAndIsDeleted(@NonNull Long templateId, @NonNull Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull TemplatePageEntity> findAllByTemplateEntity_IdAndIsActiveAndIsDeleted(@NonNull Long templateId, boolean isActive, boolean isDeleted, Pageable pageable);

    boolean existsByTemplateEntity_IdAndPageTypeEntity_IdAndIsActiveAndIsDeleted(@NonNull Long templateId, @NonNull Long pageTypeId, boolean isActive, boolean isDeleted);
}
