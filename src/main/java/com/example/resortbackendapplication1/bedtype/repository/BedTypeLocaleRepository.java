package com.example.resortbackendapplication1.bedtype.repository;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface BedTypeLocaleRepository extends
        JpaRepository<@NonNull BedTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull BedTypeLocaleEntity> {

    Optional<BedTypeLocaleEntity> findByBedTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long bedTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByBedTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long bedTypeId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull BedTypeLocaleEntity> findByBedTypeEntity_IdAndIsActiveAndIsDeleted(
            Long bedTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull BedTypeLocaleEntity> findByBedTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long bedTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select btle.localeEntity.code from BedTypeLocaleEntity btle " +
            "where btle.bedTypeEntity.id = :bedTypeId and btle.isActive = :isActive and btle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByBedTypeEntity_IdAndIsActiveAndIsDeleted(
            Long bedTypeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
