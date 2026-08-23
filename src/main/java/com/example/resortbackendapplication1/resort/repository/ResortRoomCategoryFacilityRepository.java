package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryFacilityRepository extends
        JpaRepository<@NonNull ResortRoomCategoryFacilityEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryFacilityEntity> {

    Optional<ResortRoomCategoryFacilityEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String code, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long facilityId, Boolean isActive, Boolean isDeleted);
}
