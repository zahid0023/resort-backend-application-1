package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryPriceRepository extends
        JpaRepository<@NonNull ResortRoomCategoryPriceEntity, @NonNull Long> {

    Optional<ResortRoomCategoryPriceEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    @Query("select distinct p.currencyEntity.code from ResortRoomCategoryPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.priceTypeEntity.code = :priceTypeCode "
            + "and p.isActive = :isActive and p.isDeleted = :isDeleted")
    List<String> findDistinctCurrencyCodeByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String priceTypeCode, Boolean isActive, Boolean isDeleted);

    List<ResortRoomCategoryPriceEntity> findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long currencyId, Boolean isActive, Boolean isDeleted);

    Optional<ResortRoomCategoryPriceEntity> findByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String priceTypeCode, Long currencyId, Boolean isActive, Boolean isDeleted);
}
