package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomSpecialPriceRepository extends
        JpaRepository<@NonNull ResortRoomSpecialPriceEntity, @NonNull Long> {

    Optional<ResortRoomSpecialPriceEntity> findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long id, Boolean isActive, Boolean isDeleted);

    List<ResortRoomSpecialPriceEntity> findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long currencyId, Boolean isActive, Boolean isDeleted);
}
