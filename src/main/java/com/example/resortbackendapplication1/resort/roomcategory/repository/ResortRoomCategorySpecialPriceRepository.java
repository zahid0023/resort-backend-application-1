package com.example.resortbackendapplication1.resort.roomcategory.repository;

import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategorySpecialPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategorySpecialPriceRepository extends
        JpaRepository<@NonNull ResortRoomCategorySpecialPriceEntity, @NonNull Long> {

    Optional<ResortRoomCategorySpecialPriceEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    List<ResortRoomCategorySpecialPriceEntity> findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long currencyId, Boolean isActive, Boolean isDeleted);
}
