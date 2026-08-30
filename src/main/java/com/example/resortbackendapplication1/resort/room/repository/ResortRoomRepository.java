package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomRepository extends
        JpaRepository<@NonNull ResortRoomEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomEntity> {

    Optional<ResortRoomEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    /** Backs AvailabilityService — every active room across every category of one resort, in one query. */
    List<ResortRoomEntity> findByResortRoomCategoryEntity_ResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted);

    /**
     * Application-level uniqueness check backing the same "unique per resort" rule the
     * fn_validate_resort_room_code_unique_per_resort trigger enforces at the DB level — the resort itself is
     * reached via resortRoomCategoryEntity.resortEntity, since a room row has no direct resort_id column.
     */
    boolean existsByResortRoomCategoryEntity_ResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortId, String code, Boolean isActive, Boolean isDeleted);
}
