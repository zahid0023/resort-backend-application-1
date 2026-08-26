package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryDateRangePriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class ResortRoomCategoryPriceMapper {

    /**
     * Builds a single BASE/WEEKDAY/WEEKEND row for the resort room category price-group creation flow — no
     * {@code valid_from}/{@code valid_to} (forbidden for these three types) and a fixed {@code priority} of 0.
     */
    public ResortRoomCategoryPriceEntity create(String name,
                                                BigDecimal price,
                                                PriceTypeEntity priceTypeEntity,
                                                PriceUnitEntity priceUnitEntity,
                                                CurrencyEntity currencyEntity) {
        ResortRoomCategoryPriceEntity entity = new ResortRoomCategoryPriceEntity();
        entity.setPriceTypeEntity(priceTypeEntity);
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        entity.setName(name);
        entity.setPrice(price);
        entity.setPriority(0);
        return entity;
    }

    /**
     * Builds a single HOLIDAY/SPECIAL row — always {@code valid_from}/{@code valid_to} bound, never tied to
     * days of week, {@code priority} free-form (defaults to 0).
     */
    public ResortRoomCategoryPriceEntity create(ResortRoomCategoryDateRangePriceRequest request,
                                                PriceTypeEntity priceTypeEntity,
                                                PriceUnitEntity priceUnitEntity,
                                                CurrencyEntity currencyEntity) {
        validateDateRange(request);
        ResortRoomCategoryPriceEntity entity = new ResortRoomCategoryPriceEntity();
        entity.setPriceTypeEntity(priceTypeEntity);
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setPriority(request.getPriority() != null ? request.getPriority() : 0);
        return entity;
    }

    /**
     * Updates a single HOLIDAY/SPECIAL row — currency is immutable and not touched here.
     */
    public void updateDateRange(ResortRoomCategoryPriceEntity entity, ResortRoomCategoryDateRangePriceRequest request, PriceUnitEntity priceUnitEntity) {
        validateDateRange(request);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setPriority(request.getPriority() != null ? request.getPriority() : 0);
    }

    /**
     * Validates the WKD/WKE-cannot-exceed-BASE rule and builds (but does not persist) the BASE/WEEKDAY/WEEKEND
     * entities for one currency, wiring each onto {@code resortRoomCategoryEntity} via
     * {@code addResortRoomCategoryPriceEntity}. Shared by {@code ResortRoomCategoryPriceServiceImpl}
     * (createMain/updateMain) and {@code ResortRoomCategoryServiceImpl} (per-currency price-group bundling at
     * resort room category creation time) — kept here, in the shared mapper layer, rather than on either
     * ServiceImpl, since a ServiceImpl must never call another domain's Service. Returns the built rows as
     * [base, weekday, weekend]; the caller is responsible for persisting them (a {@code saveAll}, or cascading
     * via a save of the owning {@code ResortRoomCategoryEntity}). Days of week are not handled here at all —
     * WEEKDAY/WEEKEND's day-of-week set is shared by every currency at the resort, set separately via
     * {@code ResortWeeklyScheduleService.updateWeeklySchedule}.
     */
    public List<ResortRoomCategoryPriceEntity> buildMainSet(ResortRoomCategoryMainPriceRequest request,
                                                             ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                             PriceTypeEntity basePriceTypeEntity,
                                                             PriceTypeEntity weekdayPriceTypeEntity,
                                                             PriceTypeEntity weekendPriceTypeEntity,
                                                             CurrencyEntity currencyEntity,
                                                             PriceUnitEntity basePriceUnitEntity,
                                                             PriceUnitEntity weekdayPriceUnitEntity,
                                                             PriceUnitEntity weekendPriceUnitEntity) {
        BigDecimal basePrice = request.getBasePriceRequest().getPrice();
        validateNotExceedingBase(basePrice, request.getWeekdayPrice().getPrice(), "WKD", currencyEntity);
        validateNotExceedingBase(basePrice, request.getWeekendPrice().getPrice(), "WKE", currencyEntity);

        ResortRoomCategoryPriceEntity baseEntity = attachMainPriceRow(resortRoomCategoryEntity,
                request.getBasePriceRequest().getName(), basePrice, basePriceTypeEntity, basePriceUnitEntity,
                currencyEntity);

        ResortRoomCategoryPriceEntity weekdayEntity = attachMainPriceRow(resortRoomCategoryEntity,
                request.getWeekdayPrice().getName(), request.getWeekdayPrice().getPrice(), weekdayPriceTypeEntity,
                weekdayPriceUnitEntity, currencyEntity);

        ResortRoomCategoryPriceEntity weekendEntity = attachMainPriceRow(resortRoomCategoryEntity,
                request.getWeekendPrice().getName(), request.getWeekendPrice().getPrice(), weekendPriceTypeEntity,
                weekendPriceUnitEntity, currencyEntity);

        return List.of(baseEntity, weekdayEntity, weekendEntity);
    }

    /**
     * Without this, {@code valid_from} after {@code valid_to} would only be caught by the database's
     * {@code chk_resort_room_category_price_dates} CHECK constraint at insert/update time — which has no
     * friendly-message mapping in {@code GlobalExceptionHandler}, so it would surface as an unfriendly
     * {@code 409 DATA_INTEGRITY_VIOLATION} with a raw Postgres error instead of a clean, up-front error.
     * Shared by {@link #create(ResortRoomCategoryDateRangePriceRequest, PriceTypeEntity, PriceUnitEntity,
     * CurrencyEntity)} and {@link #updateDateRange} so both Create and Update Holiday/Special are covered.
     */
    private void validateDateRange(ResortRoomCategoryDateRangePriceRequest request) {
        if (request.getValidFrom().isAfter(request.getValidTo())) {
            throw new IllegalArgumentException("valid_from must not be after valid_to");
        }
    }

    private void validateNotExceedingBase(BigDecimal basePrice, BigDecimal price, String priceTypeCode,
                                           CurrencyEntity currencyEntity) {
        if (price.compareTo(basePrice) > 0) {
            throw new IllegalArgumentException(
                    priceTypeCode + " price cannot exceed the BASE price (" + basePrice
                            + ") for currency id: " + currencyEntity.getId());
        }
    }

    private ResortRoomCategoryPriceEntity attachMainPriceRow(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                              String name, BigDecimal price,
                                                              PriceTypeEntity priceTypeEntity,
                                                              PriceUnitEntity priceUnitEntity,
                                                              CurrencyEntity currencyEntity) {
        ResortRoomCategoryPriceEntity entity = create(name, price, priceTypeEntity, priceUnitEntity, currencyEntity);
        resortRoomCategoryEntity.addResortRoomCategoryPriceEntity(entity);
        return entity;
    }

    public ResortRoomCategoryPriceDto.ResortRoomCategoryPriceDtoBuilder toDto(ResortRoomCategoryPriceEntity entity) {
        return ResortRoomCategoryPriceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .priority(entity.getPriority());
    }
}
