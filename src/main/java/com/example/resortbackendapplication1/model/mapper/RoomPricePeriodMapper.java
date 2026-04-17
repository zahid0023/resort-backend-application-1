package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.roompriceperiods.CreateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.UpdateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.model.dto.RoomPricePeriodDto;
import com.example.resortbackendapplication1.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import com.example.resortbackendapplication1.model.entity.RoomPricePeriodEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomPricePeriodMapper {

    public static RoomPricePeriodEntity fromRequest(CreateRoomPricePeriodRequest request, RoomEntity roomEntity, PriceTypeEntity priceTypeEntity) {
        RoomPricePeriodEntity entity = new RoomPricePeriodEntity();
        entity.setRoomEntity(roomEntity);
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setPrice(request.getPrice());
        entity.setPriority(request.getPriority() != null ? request.getPriority() : 1);
        entity.setPriceTypeEntity(priceTypeEntity);
        return entity;
    }

    public static void updateEntity(RoomPricePeriodEntity entity, UpdateRoomPricePeriodRequest request, PriceTypeEntity priceTypeEntity) {
        if (request.getStartDate() != null) entity.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) entity.setEndDate(request.getEndDate());
        if (request.getPrice() != null) entity.setPrice(request.getPrice());
        if (request.getPriority() != null) entity.setPriority(request.getPriority());
        if (priceTypeEntity != null) entity.setPriceTypeEntity(priceTypeEntity);
    }

    public static RoomPricePeriodDto toDto(RoomPricePeriodEntity entity) {
        return RoomPricePeriodDto.builder()
                .id(entity.getId())
                .roomId(entity.getRoomEntity().getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .price(entity.getPrice())
                .priority(entity.getPriority())
                .priceTypeId(entity.getPriceTypeEntity().getId())
                .priceTypeCode(entity.getPriceTypeEntity().getCode())
                .priceTypeName(entity.getPriceTypeEntity().getName())
                .build();
    }
}
