package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facility.locale.FacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityLocaleMapper {

    public FacilityLocaleEntity create(FacilityLocaleRequest request) {
        FacilityLocaleEntity entity = new FacilityLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityLocaleEntity entity, UpdateFacilityLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityLocaleEntity entity, FacilityLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityLocaleDto toDto(FacilityLocaleEntity entity) {
        return FacilityLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
