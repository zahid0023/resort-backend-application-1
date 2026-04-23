package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.model.dto.FacilityDto;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityMapper {

    public static FacilityEntity fromRequest(CreateFacilityRequest request, FacilityGroupEntity facilityGroup) {
        FacilityEntity entity = new FacilityEntity();
        entity.setFacilityGroupEntity(facilityGroup);
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 1);
        return entity;
    }

    public static void updateEntity(FacilityEntity entity, UpdateFacilityRequest request, FacilityGroupEntity facilityGroup) {
        if (facilityGroup != null) entity.setFacilityGroupEntity(facilityGroup);
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getIconType() != null) entity.setIconType(request.getIconType());
        if (request.getIconValue() != null) entity.setIconValue(request.getIconValue());
        if (request.getIconMeta() != null) entity.setIconMeta(request.getIconMeta());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
    }

    public static FacilityDto toDto(FacilityEntity entity) {
        return FacilityDto.builder()
                .id(entity.getId())
                .facilityGroupId(entity.getFacilityGroupEntity().getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
