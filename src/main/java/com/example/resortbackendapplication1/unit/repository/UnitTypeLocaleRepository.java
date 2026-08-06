package com.example.resortbackendapplication1.unit.repository;

import com.example.resortbackendapplication1.unit.model.entity.UnitTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface UnitTypeLocaleRepository extends
        JpaRepository<@NonNull UnitTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull UnitTypeLocaleEntity> {

    Optional<UnitTypeLocaleEntity> findByUnitTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long unitTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByUnitTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long unitTypeId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull UnitTypeLocaleEntity> findByUnitTypeEntity_IdAndIsActiveAndIsDeleted(
            Long unitTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull UnitTypeLocaleEntity> findByUnitTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long unitTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select utle.localeEntity.code from UnitTypeLocaleEntity utle " +
            "where utle.unitTypeEntity.id = :unitTypeId and utle.isActive = :isActive and utle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByUnitTypeEntity_IdAndIsActiveAndIsDeleted(
            Long unitTypeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
