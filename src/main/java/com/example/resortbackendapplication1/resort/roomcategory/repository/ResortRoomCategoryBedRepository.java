package com.example.resortbackendapplication1.resort.roomcategory.repository;

import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryBedEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryBedRepository extends
        JpaRepository<@NonNull ResortRoomCategoryBedEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryBedEntity> {

    Optional<ResortRoomCategoryBedEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndBedTypeEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long bedTypeId, Boolean isActive, Boolean isDeleted);
}
