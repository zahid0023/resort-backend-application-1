package com.example.resortbackendapplication1.resort.roomcategory.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.ResortRoomCategoryDateRangePriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.ResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryDateRangePriceDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMainPriceDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMainPriceEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategorySpecialPriceEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ResortRoomCategoryPriceMapper {

    /**
     * Builds (but does not persist) one currency's main price row, wiring it onto
     * {@code resortRoomCategoryEntity} via {@code addResortRoomCategoryMainPriceEntity}. Shared by
     * {@code ResortRoomCategoryPriceServiceImpl.createMain} and {@code ResortRoomCategoryServiceImpl} (per-
     * currency main price attached at resort room category creation time).
     */
    public ResortRoomCategoryMainPriceEntity createMain(ResortRoomCategoryMainPriceRequest request,
                                                         ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                         PriceUnitEntity priceUnitEntity,
                                                         CurrencyEntity currencyEntity) {
        validateNotExceedingBase(request.getBasePrice(), request.getWeekdayPrice(), "WKD");
        validateNotExceedingBase(request.getBasePrice(), request.getWeekendPrice(), "WKE");

        ResortRoomCategoryMainPriceEntity entity = new ResortRoomCategoryMainPriceEntity();
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        entity.setBasePrice(request.getBasePrice());
        entity.setWeekdayPrice(request.getWeekdayPrice());
        entity.setWeekendPrice(request.getWeekendPrice());
        resortRoomCategoryEntity.addResortRoomCategoryMainPriceEntity(entity);
        return entity;
    }

    /** Updates a main price row in place — currency is immutable and not touched here. */
    public void updateMain(ResortRoomCategoryMainPriceEntity entity, ResortRoomCategoryMainPriceRequest request,
                           PriceUnitEntity priceUnitEntity) {
        validateNotExceedingBase(request.getBasePrice(), request.getWeekdayPrice(), "WKD");
        validateNotExceedingBase(request.getBasePrice(), request.getWeekendPrice(), "WKE");

        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setBasePrice(request.getBasePrice());
        entity.setWeekdayPrice(request.getWeekdayPrice());
        entity.setWeekendPrice(request.getWeekendPrice());
    }

    public ResortRoomCategorySpecialPriceEntity createSpecial(ResortRoomCategoryDateRangePriceRequest request,
                                                               ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                               PriceUnitEntity priceUnitEntity,
                                                               CurrencyEntity currencyEntity) {
        validateDateRange(request);
        ResortRoomCategorySpecialPriceEntity entity = new ResortRoomCategorySpecialPriceEntity();
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        applyDateRangeFields(entity, request);
        resortRoomCategoryEntity.addResortRoomCategorySpecialPriceEntity(entity);
        return entity;
    }

    public void updateSpecial(ResortRoomCategorySpecialPriceEntity entity,
                              ResortRoomCategoryDateRangePriceRequest request,
                              PriceUnitEntity priceUnitEntity) {
        validateDateRange(request);
        entity.setPriceUnitEntity(priceUnitEntity);
        applyDateRangeFields(entity, request);
    }

    /**
     * Without this, {@code valid_from} after {@code valid_to} would only be caught by the database's
     * {@code chk_resort_room_category_special_price_dates} CHECK constraint at insert/update time — which has
     * no friendly-message mapping in {@code GlobalExceptionHandler}, so it would surface as an unfriendly
     * {@code 409 DATA_INTEGRITY_VIOLATION} with a raw Postgres error instead of a clean, up-front error.
     */
    private void validateDateRange(ResortRoomCategoryDateRangePriceRequest request) {
        if (request.getValidFrom().isAfter(request.getValidTo())) {
            throw new IllegalArgumentException("valid_from must not be after valid_to");
        }
    }

    private void validateNotExceedingBase(BigDecimal basePrice, BigDecimal price, String priceTypeCode) {
        if (price.compareTo(basePrice) > 0) {
            throw new IllegalArgumentException(
                    priceTypeCode + " price cannot exceed the BASE price (" + basePrice + ")");
        }
    }

    private void applyDateRangeFields(ResortRoomCategorySpecialPriceEntity entity, ResortRoomCategoryDateRangePriceRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setWeekdayPrice(request.getWeekdayPrice());
        entity.setWeekendPrice(request.getWeekendPrice());
        entity.setPriority(request.getPriority() != null ? request.getPriority() : 0);
    }

    public ResortRoomCategoryMainPriceDto.ResortRoomCategoryMainPriceDtoBuilder toDto(ResortRoomCategoryMainPriceEntity entity) {
        return ResortRoomCategoryMainPriceDto.builder()
                .id(entity.getId())
                .basePrice(entity.getBasePrice())
                .weekdayPrice(entity.getWeekdayPrice())
                .weekendPrice(entity.getWeekendPrice());
    }

    public ResortRoomCategoryDateRangePriceDto.ResortRoomCategoryDateRangePriceDtoBuilder toDto(ResortRoomCategorySpecialPriceEntity entity) {
        return ResortRoomCategoryDateRangePriceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .weekdayPrice(entity.getWeekdayPrice())
                .weekendPrice(entity.getWeekendPrice())
                .priority(entity.getPriority());
    }
}
