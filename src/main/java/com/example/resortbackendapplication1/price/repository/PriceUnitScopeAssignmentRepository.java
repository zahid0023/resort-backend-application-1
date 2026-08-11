package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceUnitScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceUnitScopeAssignmentRepository extends
        JpaRepository<@NonNull PriceUnitScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceUnitScopeAssignmentEntity> {

    Optional<PriceUnitScopeAssignmentEntity> findByPriceUnitEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceUnitId,
            Long priceScopeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceUnitEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceUnitId,
            Long priceScopeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
