package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityGroupLocaleRepository extends
        JpaRepository<@NonNull FacilityGroupLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityGroupLocaleEntity> {

    Optional<FacilityGroupLocaleEntity> findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
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

    Page<@NonNull FacilityGroupLocaleEntity> findByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull FacilityGroupLocaleEntity> findByFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select fgle.localeEntity.code from FacilityGroupLocaleEntity fgle " +
            "where fgle.facilityGroupEntity.id = :facilityGroupId and fgle.isActive = :isActive and fgle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Boolean isActive,
            Boolean isDeleted
    );
}
