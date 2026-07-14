package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.imagehosting.dto.request.ImageRequest;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortimage.UpdateResortImageRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortImageDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortImageEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortImageMapper {

    public ResortImageEntity create(ImageRequest request, ResortImageHostingConfigEntity config) {
        ResortImageEntity entity = new ResortImageEntity();
        entity.setResortEntity(config.getResortEntity());
        entity.setConfigEntity(config);
        entity.setExternalId(request.getPublicId());
        entity.setUrl(request.getImageUrl());
        entity.setCaption(request.getCaption());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        return entity;
    }

    public void update(ResortImageEntity entity, UpdateResortImageRequest request) {
        entity.setCaption(request.getCaption());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortImageDto toDto(ResortImageEntity entity) {
        return ResortImageDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .configId(entity.getConfigEntity().getId())
                .externalId(entity.getExternalId())
                .url(entity.getUrl())
                .caption(entity.getCaption())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
