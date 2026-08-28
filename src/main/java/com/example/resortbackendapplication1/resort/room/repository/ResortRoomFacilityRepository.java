package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomFacilityRepository extends
        JpaRepository<@NonNull ResortRoomFacilityEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomFacilityEntity> {

    Optional<ResortRoomFacilityEntity> findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortRoomId, String code, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long facilityId, Boolean isActive, Boolean isDeleted);
}
