package com.example.resortbackendapplication1.resort.facility.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.locale.ResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.locale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityGroupLocaleMapper {

    public ResortFacilityGroupLocaleEntity create(ResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupLocaleEntity entity = new ResortFacilityGroupLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityGroupLocaleEntity entity, UpdateResortFacilityGroupLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityGroupLocaleEntity entity, ResortFacilityGroupLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortFacilityGroupLocaleDto toDto(ResortFacilityGroupLocaleEntity entity) {
        return ResortFacilityGroupLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
