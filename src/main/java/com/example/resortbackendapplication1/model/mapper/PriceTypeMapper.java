package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.pricetypes.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.request.pricetypes.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.model.entity.PriceTypeEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceTypeMapper {

    public static PriceTypeEntity fromRequest(CreatePriceTypeRequest request) {
        PriceTypeEntity entity = new PriceTypeEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public static void updateEntity(PriceTypeEntity entity, UpdatePriceTypeRequest request) {
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
    }

    public static PriceTypeDto toDto(PriceTypeEntity entity) {
        return PriceTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
