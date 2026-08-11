package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceTypeScopeAssignmentRepository extends
        JpaRepository<@NonNull PriceTypeScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceTypeScopeAssignmentEntity> {

    Optional<PriceTypeScopeAssignmentEntity> findByPriceTypeEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeId,
            Long priceScopeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceTypeEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeId,
            Long priceScopeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
