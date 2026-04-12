package com.example.resortapplication1.model.mapper;

import com.example.resortapplication1.dto.request.resortaccesstypes.CreateResortAccessTypeRequest;
import com.example.resortapplication1.dto.request.resortaccesstypes.UpdateResortAccessTypeRequest;
import com.example.resortapplication1.model.dto.ResortAccessTypeDto;
import com.example.resortapplication1.model.entity.ResortAccessTypeEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortAccessTypeMapper {

    public static ResortAccessTypeEntity fromRequest(CreateResortAccessTypeRequest request) {
        ResortAccessTypeEntity entity = new ResortAccessTypeEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public static void updateEntity(ResortAccessTypeEntity entity, UpdateResortAccessTypeRequest request) {
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
    }

    public static ResortAccessTypeDto toDto(ResortAccessTypeEntity entity) {
        return ResortAccessTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
