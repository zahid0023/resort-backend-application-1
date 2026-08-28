package com.example.resortbackendapplication1.resort.roomcategory.repository;

import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMetaEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryMetaRepository extends JpaRepository<@NonNull ResortRoomCategoryMetaEntity, @NonNull Long> {

    Optional<ResortRoomCategoryMetaEntity> findByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Boolean isActive, Boolean isDeleted);
}
