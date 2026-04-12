package com.example.resortapplication1.model.mapper;

import com.example.resortapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortapplication1.model.dto.FacilityDto;
import com.example.resortapplication1.model.entity.FacilityEntity;
import com.example.resortapplication1.model.entity.FacilityGroupEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityMapper {

    public static FacilityEntity fromRequest(CreateFacilityRequest request, FacilityGroupEntity facilityGroup) {
        FacilityEntity entity = new FacilityEntity();
        entity.setFacilityGroupEntity(facilityGroup);
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setIcon(request.getIcon());
        return entity;
    }

    public static void updateEntity(FacilityEntity entity, UpdateFacilityRequest request, FacilityGroupEntity facilityGroup) {
        if (facilityGroup != null) entity.setFacilityGroupEntity(facilityGroup);
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getIcon() != null) entity.setIcon(request.getIcon());
    }

    public static FacilityDto toDto(FacilityEntity entity) {
        return FacilityDto.builder()
                .id(entity.getId())
                .facilityGroupId(entity.getFacilityGroupEntity().getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .icon(entity.getIcon())
                .build();
    }
}
