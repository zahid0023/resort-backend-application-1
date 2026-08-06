package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityLocaleRepository extends
        JpaRepository<@NonNull FacilityLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityLocaleEntity> {

    Optional<FacilityLocaleEntity> findByFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
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

    Page<@NonNull FacilityLocaleEntity> findByFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull FacilityLocaleEntity> findByFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long facilityId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select fle.localeEntity.code from FacilityLocaleEntity fle " +
            "where fle.facilityEntity.id = :facilityId and fle.isActive = :isActive and fle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
            Boolean isActive,
            Boolean isDeleted
    );
}
