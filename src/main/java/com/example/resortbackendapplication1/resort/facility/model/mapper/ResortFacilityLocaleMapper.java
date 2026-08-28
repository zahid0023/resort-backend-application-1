package com.example.resortbackendapplication1.resort.facility.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.locale.ResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.locale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityLocaleMapper {

    public ResortFacilityLocaleEntity create(ResortFacilityLocaleRequest request) {
        ResortFacilityLocaleEntity entity = new ResortFacilityLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityLocaleEntity entity, UpdateResortFacilityLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityLocaleEntity entity, ResortFacilityLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setNotes(request.getNotes() == null ? "" : request.getNotes());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortFacilityLocaleDto toDto(ResortFacilityLocaleEntity entity) {
        return ResortFacilityLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .notes(entity.getNotes())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
