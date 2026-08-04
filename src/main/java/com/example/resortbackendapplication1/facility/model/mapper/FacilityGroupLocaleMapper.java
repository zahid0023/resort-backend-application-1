package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.FacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityGroupLocaleMapper {

    public FacilityGroupLocaleEntity create(FacilityGroupLocaleRequest request) {
        FacilityGroupLocaleEntity entity = new FacilityGroupLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityGroupLocaleEntity entity, UpdateFacilityGroupLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityGroupLocaleEntity entity, FacilityGroupLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityGroupLocaleDto toDto(FacilityGroupLocaleEntity entity) {
        return FacilityGroupLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
