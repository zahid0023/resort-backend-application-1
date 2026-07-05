package com.example.resortbackendapplication1.imagehosting.model.mapper;

import com.example.resortbackendapplication1.imagehosting.dto.request.CreateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.ImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.UpdateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.model.dto.ResortImageHostingConfigDto;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortImageHostingConfigMapper {

    public ResortImageHostingConfigEntity create(CreateResortImageHostingConfigRequest request) {
        ResortImageHostingConfigEntity entity = new ResortImageHostingConfigEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortImageHostingConfigEntity entity,
                       UpdateResortImageHostingConfigRequest request) {
        entity.setName(request.getName());
    }

    private void applyCommonFields(ResortImageHostingConfigEntity entity,
                                   ImageHostingConfigRequest request) {
        entity.setName(request.getName());
        entity.setProvider(request.getProvider());
        entity.setConfig(request.getConfig());
    }

    public ResortImageHostingConfigDto toDto(ResortImageHostingConfigEntity entity) {
        return ResortImageHostingConfigDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .provider(entity.getProvider())
                .config(entity.getConfig())
                .build();
    }
}
