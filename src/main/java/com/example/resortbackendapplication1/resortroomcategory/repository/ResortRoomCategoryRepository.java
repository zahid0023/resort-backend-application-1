package com.example.resortbackendapplication1.resortroomcategory.repository;

import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ResortRoomCategoryRepository extends JpaRepository<@NonNull ResortRoomCategoryEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryEntity> {

    Optional<ResortRoomCategoryEntity> findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortId, Boolean isActive, Boolean isDeleted);
}
