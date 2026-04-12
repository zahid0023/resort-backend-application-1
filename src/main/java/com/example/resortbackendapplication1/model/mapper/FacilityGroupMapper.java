package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityGroupMapper {

    public static FacilityGroupEntity fromRequest(CreateFacilityGroupRequest request) {
        FacilityGroupEntity entity = new FacilityGroupEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        return entity;
    }

    public static void updateEntity(FacilityGroupEntity entity, UpdateFacilityGroupRequest request) {
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
    }

    public static FacilityGroupDto toDto(FacilityGroupEntity entity) {
        return FacilityGroupDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
