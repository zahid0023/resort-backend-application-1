package com.example.resortbackendapplication1.resort.core.repository;

import com.example.resortbackendapplication1.resort.core.model.entity.ResortBasicInfoLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortBasicInfoLocaleRepository extends JpaRepository<@NonNull ResortBasicInfoLocaleEntity, @NonNull Long> {

    Optional<ResortBasicInfoLocaleEntity> findByResortBasicInfoEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortBasicInfoId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortBasicInfoEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortBasicInfoId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortBasicInfoLocaleEntity> findByResortBasicInfoEntity_IdAndIsActiveAndIsDeleted(
            Long resortBasicInfoId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortBasicInfoLocaleEntity> findByResortBasicInfoEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortBasicInfoId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rbil.localeEntity.code from ResortBasicInfoLocaleEntity rbil "
            + "where rbil.resortBasicInfoEntity.id = :resortBasicInfoId "
            + "and rbil.isActive = :isActive and rbil.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortBasicInfoEntity_IdAndIsActiveAndIsDeleted(
            Long resortBasicInfoId, Boolean isActive, Boolean isDeleted);

}
