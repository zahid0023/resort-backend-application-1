package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.dto.request.resortimages.UpdateResortImageRequest;
import com.example.resortbackendapplication1.model.dto.ResortImageDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortImageMapper {

    public static ResortImageEntity fromRequest(ImageRequest imageRequest, ResortEntity resortEntity) {
        ResortImageEntity entity = new ResortImageEntity();
        entity.setResortEntity(resortEntity);
        entity.setImageUrl(imageRequest.getImageUrl());
        entity.setPublicId(imageRequest.getPublicId());
        entity.setCaption(imageRequest.getCaption());
        entity.setIsDefault(imageRequest.getIsDefault() != null ? imageRequest.getIsDefault() : false);
        entity.setSortOrder(imageRequest.getSortOrder() != null ? imageRequest.getSortOrder() : 0);
        return entity;
    }

    public static void updateEntity(ResortImageEntity entity, UpdateResortImageRequest request) {
        if (request.getCaption() != null) entity.setCaption(request.getCaption());
        if (request.getIsDefault() != null) entity.setIsDefault(request.getIsDefault());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
    }

    public static ResortImageDto toDto(ResortImageEntity entity) {
        return ResortImageDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .imageUrl(entity.getImageUrl())
                .publicId(entity.getPublicId())
                .caption(entity.getCaption())
                .isDefault(entity.getIsDefault())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
