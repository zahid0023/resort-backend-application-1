package com.example.resortbackendapplication1.resortroomcategoryprice.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceDayEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@UtilityClass
public class ResortRoomCategoryPriceMapper {

    public ResortRoomCategoryPriceEntity create(CreateResortRoomCategoryPriceRequest request,
                                                ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                PriceTypeEntity priceTypeEntity,
                                                PriceUnitEntity priceUnitEntity,
                                                CurrencyEntity currencyEntity,
                                                int priority,
                                                List<DayOfWeekEntity> dayEntities) {
        ResortRoomCategoryPriceEntity entity = new ResortRoomCategoryPriceEntity();
        entity.setResortRoomCategoryEntity(resortRoomCategoryEntity);
        entity.setPriceTypeEntity(priceTypeEntity);
        entity.setCurrencyEntity(currencyEntity);
        applyCommonFields(entity, request, priceUnitEntity, priority, dayEntities);
        return entity;
    }

    public void update(ResortRoomCategoryPriceEntity entity,
                       UpdateResortRoomCategoryPriceRequest request,
                       PriceUnitEntity priceUnitEntity,
                       int priority,
                       List<DayOfWeekEntity> dayEntities) {
        applyCommonFields(entity, request, priceUnitEntity, priority, dayEntities);
    }

    private void applyCommonFields(ResortRoomCategoryPriceEntity entity,
                                   ResortRoomCategoryPriceRequest request,
                                   PriceUnitEntity priceUnitEntity,
                                   int priority,
                                   List<DayOfWeekEntity> dayEntities) {
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setPriority(priority);
        mapDays(entity, dayEntities);
    }

    private void mapDays(ResortRoomCategoryPriceEntity entity, List<DayOfWeekEntity> dayEntities) {
        entity.getDays().clear();
        for (DayOfWeekEntity day : dayEntities) {
            ResortRoomCategoryPriceDayEntity dayEntity = new ResortRoomCategoryPriceDayEntity();
            dayEntity.setResortRoomCategoryPriceEntity(entity);
            dayEntity.setDayOfWeekEntity(day);
            entity.getDays().add(dayEntity);
        }
    }

    public ResortRoomCategoryPriceDto toDto(ResortRoomCategoryPriceEntity entity) {
        return toDto(entity, null);
    }

    public ResortRoomCategoryPriceDto toDto(ResortRoomCategoryPriceEntity entity, BigDecimal basePrice) {
        BigDecimal discountAmount = null;
        BigDecimal discountPercentage = null;
        if (basePrice != null && entity.getPrice().compareTo(basePrice) < 0) {
            discountAmount = basePrice.subtract(entity.getPrice()).setScale(2, RoundingMode.HALF_UP);
            if (basePrice.compareTo(BigDecimal.ZERO) > 0) {
                discountPercentage = discountAmount
                        .divide(basePrice, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }
        }
        List<Long> dayIds = entity.getDays().stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(d -> d.getDayOfWeekEntity().getId())
                .toList();
        return ResortRoomCategoryPriceDto.builder()
                .id(entity.getId())
                .priceType(PriceTypeMapper.toDto(entity.getPriceTypeEntity()))
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()))
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .priority(entity.getPriority())
                .dayOfWeekIds(dayIds.isEmpty() ? null : dayIds)
                .discountAmount(discountAmount)
                .discountPercentage(discountPercentage)
                .build();
    }
}
