package com.example.resortbackendapplication1.pagetype.repository;

import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PageTypeLocaleRepository extends JpaRepository<@NonNull PageTypeLocaleEntity, @NonNull Long> {

    Optional<PageTypeLocaleEntity> findByPageTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long pageTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
