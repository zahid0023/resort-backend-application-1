package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.ResortRoomDateRangePriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.ResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryDateRangePriceDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMainPriceDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDateRangePriceDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomMainPriceDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMainPriceEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ResortRoomPriceMapper {

    /**
     * Builds (but does not persist) one currency's main price override row, wiring it onto
     * {@code resortRoomEntity} via {@code addResortRoomMainPriceEntity}.
     */
    public ResortRoomMainPriceEntity createMain(ResortRoomMainPriceRequest request,
                                                ResortRoomEntity resortRoomEntity,
                                                PriceUnitEntity priceUnitEntity,
                                                CurrencyEntity currencyEntity) {
        validateNotExceedingBase(request.getBasePrice(), request.getWeekdayPrice(), "WKD");
        validateNotExceedingBase(request.getBasePrice(), request.getWeekendPrice(), "WKE");

        ResortRoomMainPriceEntity entity = new ResortRoomMainPriceEntity();
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        entity.setBasePrice(request.getBasePrice());
        entity.setWeekdayPrice(request.getWeekdayPrice());
        entity.setWeekendPrice(request.getWeekendPrice());
        resortRoomEntity.addResortRoomMainPriceEntity(entity);
        return entity;
    }

    /** Updates a main price override row in place — currency is immutable and not touched here. */
    public void updateMain(ResortRoomMainPriceEntity entity, ResortRoomMainPriceRequest request,
                           PriceUnitEntity priceUnitEntity) {
        validateNotExceedingBase(request.getBasePrice(), request.getWeekdayPrice(), "WKD");
        validateNotExceedingBase(request.getBasePrice(), request.getWeekendPrice(), "WKE");

        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setBasePrice(request.getBasePrice());
        entity.setWeekdayPrice(request.getWeekdayPrice());
        entity.setWeekendPrice(request.getWeekendPrice());
    }

    public ResortRoomSpecialPriceEntity createSpecial(ResortRoomDateRangePriceRequest request,
                                                       ResortRoomEntity resortRoomEntity,
                                                       PriceUnitEntity priceUnitEntity,
                                                       CurrencyEntity currencyEntity) {
        validateDateRange(request);
        ResortRoomSpecialPriceEntity entity = new ResortRoomSpecialPriceEntity();
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        applyDateRangeFields(entity, request);
        resortRoomEntity.addResortRoomSpecialPriceEntity(entity);
        return entity;
    }

    public void updateSpecial(ResortRoomSpecialPriceEntity entity,
                              ResortRoomDateRangePriceRequest request,
                              PriceUnitEntity priceUnitEntity) {
        validateDateRange(request);
        entity.setPriceUnitEntity(priceUnitEntity);
        applyDateRangeFields(entity, request);
    }

    private void validateDateRange(ResortRoomDateRangePriceRequest request) {
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

    private void applyDateRangeFields(ResortRoomSpecialPriceEntity entity, ResortRoomDateRangePriceRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setWeekdayPrice(request.getWeekdayPrice());
        entity.setWeekendPrice(request.getWeekendPrice());
        entity.setPriority(request.getPriority() != null ? request.getPriority() : 0);
    }

    public ResortRoomMainPriceDto.ResortRoomMainPriceDtoBuilder toDto(ResortRoomMainPriceEntity entity) {
        return ResortRoomMainPriceDto.builder()
                .id(entity.getId())
                .basePrice(entity.getBasePrice())
                .weekdayPrice(entity.getWeekdayPrice())
                .weekendPrice(entity.getWeekendPrice());
    }

    public ResortRoomDateRangePriceDto.ResortRoomDateRangePriceDtoBuilder toDto(ResortRoomSpecialPriceEntity entity) {
        return ResortRoomDateRangePriceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .weekdayPrice(entity.getWeekdayPrice())
                .weekendPrice(entity.getWeekendPrice())
                .priority(entity.getPriority());
    }

    /**
     * Converts a room CATEGORY's main price bundle into the room-price DTO shape, for the "this room has no
     * override, inherit the category's price for this currency" fallback path in
     * {@code ResortRoomPriceServiceImpl.getAllGroupedByCurrency}. {@code resortRoom} is left {@code null} —
     * this bundle isn't the room's own row. Lives here (not in the ServiceImpl) because a ServiceImpl must
     * never depend on another entity's DTOs/services directly; converting between two Mappers' DTO shapes is
     * the shared-logic escape hatch for that.
     */
    public ResortRoomMainPriceDto fromCategoryMain(ResortRoomCategoryMainPriceDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return ResortRoomMainPriceDto.builder()
                .id(categoryDto.getId())
                .priceUnit(categoryDto.getPriceUnit())
                .currency(categoryDto.getCurrency())
                .basePrice(categoryDto.getBasePrice())
                .weekdayPrice(categoryDto.getWeekdayPrice())
                .weekendPrice(categoryDto.getWeekendPrice())
                .weekdayDays(categoryDto.getWeekdayDays())
                .weekendDays(categoryDto.getWeekendDays())
                .build();
    }

    public ResortRoomDateRangePriceDto fromCategoryDateRange(ResortRoomCategoryDateRangePriceDto categoryDto) {
        return ResortRoomDateRangePriceDto.builder()
                .id(categoryDto.getId())
                .priceUnit(categoryDto.getPriceUnit())
                .currency(categoryDto.getCurrency())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .validFrom(categoryDto.getValidFrom())
                .validTo(categoryDto.getValidTo())
                .weekdayPrice(categoryDto.getWeekdayPrice())
                .weekendPrice(categoryDto.getWeekendPrice())
                .priority(categoryDto.getPriority())
                .weekdayDays(categoryDto.getWeekdayDays())
                .weekendDays(categoryDto.getWeekendDays())
                .build();
    }
}
