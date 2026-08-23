package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryFacilityGroupRepository extends
        JpaRepository<@NonNull ResortRoomCategoryFacilityGroupEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryFacilityGroupEntity> {

    Optional<ResortRoomCategoryFacilityGroupEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long facilityGroupId, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String code, Boolean isActive, Boolean isDeleted);
}
