package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomBedRepository extends
        JpaRepository<@NonNull ResortRoomBedEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomBedEntity> {

    Optional<ResortRoomBedEntity> findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndBedTypeEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long bedTypeId, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Boolean isActive, Boolean isDeleted);
}
