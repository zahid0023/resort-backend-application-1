package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.FacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityScopeMapper {

    public void update(FacilityScopeEntity entity, UpdateFacilityScopeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityScopeEntity entity, FacilityScopeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityScopeDto toDto(FacilityScopeEntity entity) {
        List<FacilityScopeLocaleDto> locales = entity.getFacilityScopeLocaleEntities().stream()
                .filter(l -> Boolean.TRUE.equals(l.getIsActive()) && Boolean.FALSE.equals(l.getIsDeleted()))
                .map(FacilityScopeLocaleMapper::toDto)
                .toList();

        return FacilityScopeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
