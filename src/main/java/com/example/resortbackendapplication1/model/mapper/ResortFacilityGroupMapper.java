package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityGroupMapper {

    public static ResortFacilityGroupEntity fromRequest(CreateResortFacilityGroupRequest request,
                                                        ResortEntity resortEntity,
                                                        FacilityGroupEntity facilityGroupEntity) {
        ResortFacilityGroupEntity entity = new ResortFacilityGroupEntity();
        entity.setResortEntity(resortEntity);
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        return entity;
    }

    public static void updateEntity(ResortFacilityGroupEntity entity,
                                    UpdateResortFacilityGroupRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
    }

    public static ResortFacilityGroupDto toDto(ResortFacilityGroupEntity entity) {
        return ResortFacilityGroupDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .facilityGroupId(entity.getFacilityGroupEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
