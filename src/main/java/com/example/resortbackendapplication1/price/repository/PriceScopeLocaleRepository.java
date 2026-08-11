package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceScopeLocaleRepository extends
        JpaRepository<@NonNull PriceScopeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceScopeLocaleEntity> {

    Optional<PriceScopeLocaleEntity> findByPriceScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long priceScopeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPriceScopeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long priceScopeId,
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

    Page<@NonNull PriceScopeLocaleEntity> findByPriceScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceScopeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PriceScopeLocaleEntity> findByPriceScopeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long priceScopeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select psle.localeEntity.code from PriceScopeLocaleEntity psle " +
            "where psle.priceScopeEntity.id = :priceScopeId and psle.isActive = :isActive and psle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByPriceScopeEntity_IdAndIsActiveAndIsDeleted(
            Long priceScopeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
