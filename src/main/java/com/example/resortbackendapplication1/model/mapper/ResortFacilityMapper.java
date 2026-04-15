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
                                                   FacilityEntity facilityEntity,
                                                   ResortFacilityGroupEntity resortFacilityGroupEntity) {
        ResortFacilityEntity entity = new ResortFacilityEntity();
        entity.setFacilityEntity(facilityEntity);
        entity.setResortFacilityGroupEntity(resortFacilityGroupEntity);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setIcon(request.getIcon());
        entity.setValue(request.getValue());
        return entity;
    }

    public static void updateEntity(ResortFacilityEntity entity, UpdateResortFacilityRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getIcon() != null) entity.setIcon(request.getIcon());
        if (request.getValue() != null) entity.setValue(request.getValue());
    }

    public static ResortFacilityDto toDto(ResortFacilityEntity entity) {
        return ResortFacilityDto.builder()
                .id(entity.getId())
                .resortFacilityGroupId(entity.getResortFacilityGroupEntity().getId())
                .facilityId(entity.getFacilityEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .value(entity.getValue())
                .build();
    }
}
