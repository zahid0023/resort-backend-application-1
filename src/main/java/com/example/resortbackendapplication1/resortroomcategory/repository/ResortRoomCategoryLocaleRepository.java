package com.example.resortbackendapplication1.resortroomcategory.repository;

import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortRoomCategoryLocaleRepository extends JpaRepository<@NonNull ResortRoomCategoryLocaleEntity, @NonNull Long> {

    Optional<ResortRoomCategoryLocaleEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);
}
