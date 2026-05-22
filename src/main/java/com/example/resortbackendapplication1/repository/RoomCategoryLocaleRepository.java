package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.RoomCategoryLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomCategoryLocaleRepository extends JpaRepository<@NonNull RoomCategoryLocaleEntity, @NonNull Long> {
    Optional<RoomCategoryLocaleEntity> findByRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long roomCategoryId, Long id, Boolean isActive, Boolean isDeleted);
}
