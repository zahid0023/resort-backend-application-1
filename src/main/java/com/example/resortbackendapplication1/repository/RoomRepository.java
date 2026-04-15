package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.RoomEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<@NonNull RoomEntity, @NonNull Long> {

    Optional<RoomEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(Long resortRoomCategoryId, Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull RoomEntity> findAllByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(Long resortRoomCategoryId, boolean isActive, boolean isDeleted, Pageable pageable);
}
