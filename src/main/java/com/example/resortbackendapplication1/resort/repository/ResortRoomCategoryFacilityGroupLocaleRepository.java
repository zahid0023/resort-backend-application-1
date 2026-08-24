package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryFacilityGroupLocaleRepository extends JpaRepository<@NonNull ResortRoomCategoryFacilityGroupLocaleEntity, @NonNull Long> {

    Optional<ResortRoomCategoryFacilityGroupLocaleEntity> findByResortRoomCategoryFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityGroupId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityGroupId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortRoomCategoryFacilityGroupLocaleEntity> findByResortRoomCategoryFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityGroupId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortRoomCategoryFacilityGroupLocaleEntity> findByResortRoomCategoryFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityGroupId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rrcfgl.localeEntity.code from ResortRoomCategoryFacilityGroupLocaleEntity rrcfgl "
            + "where rrcfgl.resortRoomCategoryFacilityGroupEntity.id = :resortRoomCategoryFacilityGroupId "
            + "and rrcfgl.isActive = :isActive and rrcfgl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortRoomCategoryFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryFacilityGroupId, Boolean isActive, Boolean isDeleted);
}
