package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortFacilityLocaleRepository extends JpaRepository<@NonNull ResortFacilityLocaleEntity, @NonNull Long> {

    Optional<ResortFacilityLocaleEntity> findByResortFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortFacilityLocaleEntity> findByResortFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortFacilityLocaleEntity> findByResortFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortFacilityId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rfl.localeEntity.code from ResortFacilityLocaleEntity rfl "
            + "where rfl.resortFacilityEntity.id = :resortFacilityId "
            + "and rfl.isActive = :isActive and rfl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Boolean isActive, Boolean isDeleted);
}
