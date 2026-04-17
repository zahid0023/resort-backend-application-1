package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.PriceTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceTypeRepository extends JpaRepository<@NonNull PriceTypeEntity, Long> {
    Optional<PriceTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, boolean isActive, boolean isDeleted);
    Page<@NonNull PriceTypeEntity> findAllByIsActiveAndIsDeleted(boolean isActive, boolean isDeleted, Pageable pageable);
}
