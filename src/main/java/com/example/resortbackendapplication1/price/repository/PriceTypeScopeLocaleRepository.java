package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceTypeScopeLocaleRepository extends
        JpaRepository<@NonNull PriceTypeScopeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceTypeScopeLocaleEntity> {

    Optional<PriceTypeScopeLocaleEntity> findByPriceTypeScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceTypeScopeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
            Long localeId,
            String name,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
            Long localeId,
            String name,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull PriceTypeScopeLocaleEntity> findByPriceTypeScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PriceTypeScopeLocaleEntity> findByPriceTypeScopeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long priceTypeScopeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
