package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceTypeLocaleRepository extends
        JpaRepository<@NonNull PriceTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceTypeLocaleEntity> {

    Optional<PriceTypeLocaleEntity> findByPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long priceTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeId,
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

    Page<@NonNull PriceTypeLocaleEntity> findByPriceTypeEntity_IdAndIsActiveAndIsDeleted(
            Long priceTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PriceTypeLocaleEntity> findByPriceTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long priceTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
