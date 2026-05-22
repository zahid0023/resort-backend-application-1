package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.resortfacilities.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityMapper {

    public static ResortFacilityEntity fromRequest(CreateResortFacilityRequest request,
                                                   ResortFacilityGroupEntity resortFacilityGroupEntity,
                                                   FacilityEntity facilityEntity) {
        ResortFacilityEntity entity = new ResortFacilityEntity();
        entity.setResortFacilityGroupEntity(resortFacilityGroupEntity);
        entity.setFacilityEntity(facilityEntity);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
        return entity;
    }

    public static void updateEntity(ResortFacilityEntity entity, UpdateResortFacilityRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
        if (request.getIconType() != null) entity.setIconType(request.getIconType());
        if (request.getIconValue() != null) entity.setIconValue(request.getIconValue());
        if (request.getIconMeta() != null) entity.setIconMeta(request.getIconMeta());
    }

    public static ResortFacilityDto toDto(ResortFacilityEntity entity) {
        return ResortFacilityDto.builder()
                .id(entity.getId())
                .resortFacilityGroupId(entity.getResortFacilityGroupEntity().getId())
                .facilityId(entity.getFacilityEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .build();
    }
}
