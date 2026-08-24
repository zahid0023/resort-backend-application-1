package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortFacilityGroupLocaleRepository extends JpaRepository<@NonNull ResortFacilityGroupLocaleEntity, @NonNull Long> {

    Optional<ResortFacilityGroupLocaleEntity> findByResortFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortFacilityGroupId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityGroupId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortFacilityGroupLocaleEntity> findByResortFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityGroupId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortFacilityGroupLocaleEntity> findByResortFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortFacilityGroupId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rfgl.localeEntity.code from ResortFacilityGroupLocaleEntity rfgl "
            + "where rfgl.resortFacilityGroupEntity.id = :resortFacilityGroupId "
            + "and rfgl.isActive = :isActive and rfgl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityGroupId, Boolean isActive, Boolean isDeleted);
}
