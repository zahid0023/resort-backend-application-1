package com.example.resortbackendapplication1.resortroomcategory.repository;

import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortRoomCategoryMetaRepository extends JpaRepository<ResortRoomCategoryMetaEntity, Long> {

    Optional<ResortRoomCategoryMetaEntity> findByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Boolean isActive, Boolean isDeleted);
}
