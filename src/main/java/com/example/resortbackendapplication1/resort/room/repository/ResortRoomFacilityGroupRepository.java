package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomFacilityGroupRepository extends
        JpaRepository<@NonNull ResortRoomFacilityGroupEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomFacilityGroupEntity> {

    Optional<ResortRoomFacilityGroupEntity> findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long facilityGroupId, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortRoomId, String code, Boolean isActive, Boolean isDeleted);
}
