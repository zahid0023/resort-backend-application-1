package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.FacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityScopeLocaleMapper {

    public FacilityScopeLocaleEntity create(FacilityScopeLocaleRequest request) {
        FacilityScopeLocaleEntity entity = new FacilityScopeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityScopeLocaleEntity entity, UpdateFacilityScopeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityScopeLocaleEntity entity, FacilityScopeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityScopeLocaleDto toDto(FacilityScopeLocaleEntity entity) {
        return FacilityScopeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
