package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceUnitLocaleRepository extends
        JpaRepository<@NonNull PriceUnitLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceUnitLocaleEntity> {

    Optional<PriceUnitLocaleEntity> findByPriceUnitEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long priceUnitId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceUnitEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long priceUnitId,
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

    Page<@NonNull PriceUnitLocaleEntity> findByPriceUnitEntity_IdAndIsActiveAndIsDeleted(
            Long priceUnitId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PriceUnitLocaleEntity> findByPriceUnitEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long priceUnitId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select pule.localeEntity.code from PriceUnitLocaleEntity pule " +
            "where pule.priceUnitEntity.id = :priceUnitId and pule.isActive = :isActive and pule.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByPriceUnitEntity_IdAndIsActiveAndIsDeleted(
            Long priceUnitId,
            Boolean isActive,
            Boolean isDeleted
    );
}
