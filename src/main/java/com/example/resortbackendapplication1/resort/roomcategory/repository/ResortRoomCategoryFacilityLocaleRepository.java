package com.example.resortbackendapplication1.resort.roomcategory.repository;

import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryFacilityLocaleRepository extends JpaRepository<@NonNull ResortRoomCategoryFacilityLocaleEntity, @NonNull Long> {

    Optional<ResortRoomCategoryFacilityLocaleEntity> findByResortRoomCategoryFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortRoomCategoryFacilityLocaleEntity> findByResortRoomCategoryFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortRoomCategoryFacilityLocaleEntity> findByResortRoomCategoryFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rrcfl.localeEntity.code from ResortRoomCategoryFacilityLocaleEntity rrcfl "
            + "where rrcfl.resortRoomCategoryFacilityEntity.id = :resortRoomCategoryFacilityId "
            + "and rrcfl.isActive = :isActive and rrcfl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortRoomCategoryFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityId, Boolean isActive, Boolean isDeleted);
}
