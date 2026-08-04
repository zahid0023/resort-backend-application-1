package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceTypeScopeAssignmentRepository extends
        JpaRepository<@NonNull PriceTypeScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceTypeScopeAssignmentEntity> {

    Optional<PriceTypeScopeAssignmentEntity> findByPriceTypeScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceTypeScopeEntity_IdAndPriceTypeEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            Long priceTypeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull PriceTypeScopeAssignmentEntity> findByPriceTypeScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
