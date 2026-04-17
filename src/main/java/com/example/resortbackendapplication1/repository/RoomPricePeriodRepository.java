package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.RoomPricePeriodEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomPricePeriodRepository extends JpaRepository<@NonNull RoomPricePeriodEntity, @NonNull Long> {

    Optional<RoomPricePeriodEntity> findByRoomEntity_IdAndIdAndIsActiveAndIsDeleted(Long roomId, Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull RoomPricePeriodEntity> findAllByRoomEntity_IdAndIsActiveAndIsDeleted(Long roomId, boolean isActive, boolean isDeleted, Pageable pageable);
}
