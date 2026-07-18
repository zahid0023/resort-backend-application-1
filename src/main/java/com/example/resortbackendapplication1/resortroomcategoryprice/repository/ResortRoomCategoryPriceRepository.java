package com.example.resortbackendapplication1.resortroomcategoryprice.repository;

import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ResortRoomCategoryPriceRepository extends
        JpaRepository<@NonNull ResortRoomCategoryPriceEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryPriceEntity> {

    Optional<ResortRoomCategoryPriceEntity> findByIdAndResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortRoomCategoryId, Boolean isActive, Boolean isDeleted);
}
