package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.UpdateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.model.dto.ResortImageStorageConfigDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageStorageConfigEntity;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ResortImageStorageConfigMapper {

    public static ResortImageStorageConfigEntity fromRequest(CreateResortImageStorageConfigRequest request,
                                                             ResortEntity resortEntity) {
        ResortImageStorageConfigEntity entity = new ResortImageStorageConfigEntity();
        entity.setResortEntity(resortEntity);
        entity.setProvider(request.getProvider());
        entity.setConfig(Map.copyOf(request.getConfig()));
        return entity;
    }

    public static void updateEntity(ResortImageStorageConfigEntity entity,
                                    UpdateResortImageStorageConfigRequest request) {
        if (request.getProvider() != null) entity.setProvider(request.getProvider());
        if (request.getConfig() != null) entity.setConfig(Map.copyOf(request.getConfig()));
    }

    public static ResortImageStorageConfigDto toDto(ResortImageStorageConfigEntity entity) {
        return ResortImageStorageConfigDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .provider(entity.getProvider().name())
                .config(entity.getConfig())
                .build();
    }
}
