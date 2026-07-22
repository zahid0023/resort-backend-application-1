package com.example.resortbackendapplication1.resortroomcategory.repository;

import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryBedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortRoomCategoryBedRepository extends JpaRepository<ResortRoomCategoryBedEntity, Long> {

    Optional<ResortRoomCategoryBedEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);
}
