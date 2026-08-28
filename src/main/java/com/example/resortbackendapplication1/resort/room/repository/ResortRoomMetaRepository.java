package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomMetaRepository extends JpaRepository<@NonNull ResortRoomMetaEntity, @NonNull Long> {

    Optional<ResortRoomMetaEntity> findByResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Boolean isActive, Boolean isDeleted);
}
