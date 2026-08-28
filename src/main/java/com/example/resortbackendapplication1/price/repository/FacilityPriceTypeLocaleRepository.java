package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityPriceTypeLocaleRepository extends
        JpaRepository<@NonNull FacilityPriceTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityPriceTypeLocaleEntity> {

    Optional<FacilityPriceTypeLocaleEntity> findByFacilityPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityPriceTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityPriceTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long facilityPriceTypeId,
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

    Page<@NonNull FacilityPriceTypeLocaleEntity> findByFacilityPriceTypeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityPriceTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull FacilityPriceTypeLocaleEntity> findByFacilityPriceTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long facilityPriceTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select ptle.localeEntity.code from FacilityPriceTypeLocaleEntity ptle " +
            "where ptle.facilityPriceTypeEntity.id = :facilityPriceTypeId and ptle.isActive = :isActive and ptle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByFacilityPriceTypeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityPriceTypeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
