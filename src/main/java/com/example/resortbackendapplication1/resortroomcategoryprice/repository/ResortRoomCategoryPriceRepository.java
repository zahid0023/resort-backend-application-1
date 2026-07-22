package com.example.resortbackendapplication1.resortroomcategoryprice.repository;

import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResortRoomCategoryPriceRepository extends
        JpaRepository<@NonNull ResortRoomCategoryPriceEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryPriceEntity> {

    Optional<ResortRoomCategoryPriceEntity> findByIdAndResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortRoomCategoryId, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomCategoryEntity_IdAndPriceTypeEntity_IdAndCurrencyEntity_IdAndIsDeletedFalse(
            Long resortRoomCategoryId, Long priceTypeId, Long currencyId);

    boolean existsByResortRoomCategoryEntity_IdAndPriceTypeEntity_IdAndCurrencyEntity_IdAndIsDeletedFalseAndIdNot(
            Long resortRoomCategoryId, Long priceTypeId, Long currencyId, Long excludeId);

    @Query("SELECT p FROM ResortRoomCategoryPriceEntity p " +
            "WHERE p.resortRoomCategoryEntity.id = :categoryId " +
            "AND p.currencyEntity.id = :currencyId " +
            "AND p.priceTypeEntity.code = 'BAS' " +
            "AND p.isActive = true AND p.isDeleted = false")
    Optional<ResortRoomCategoryPriceEntity> findBasePrice(
            @Param("categoryId") Long categoryId,
            @Param("currencyId") Long currencyId);

    @Query("SELECT COUNT(DISTINCT p.priceTypeEntity.code) FROM ResortRoomCategoryPriceEntity p " +
            "WHERE p.resortRoomCategoryEntity.id = :categoryId " +
            "AND p.currencyEntity.id = :currencyId " +
            "AND p.priceTypeEntity.code IN ('BAS', 'WKD', 'WKE') " +
            "AND p.isActive = true AND p.isDeleted = false")
    long countFoundationPrices(
            @Param("categoryId") Long categoryId,
            @Param("currencyId") Long currencyId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ResortRoomCategoryPriceEntity p " +
            "WHERE p.resortRoomCategoryEntity.id = :categoryId " +
            "AND p.currencyEntity.id = :currencyId " +
            "AND p.priceTypeEntity.code = :code " +
            "AND p.isActive = true AND p.isDeleted = false")
    boolean existsByTypeCode(
            @Param("categoryId") Long categoryId,
            @Param("currencyId") Long currencyId,
            @Param("code") String code);
}
