package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryDateBoundPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

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
    public ResortRoomCategoryPriceEntity create(ResortRoomCategoryDateBoundPriceRequest request,
                                                PriceTypeEntity priceTypeEntity,
                                                PriceUnitEntity priceUnitEntity,
                                                CurrencyEntity currencyEntity) {
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

    public void update(ResortRoomCategoryPriceEntity entity, UpdateResortRoomCategoryPriceRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryPriceEntity entity, ResortRoomCategoryPriceRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setPriority(request.getPriority() != null ? request.getPriority() : 0);
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
