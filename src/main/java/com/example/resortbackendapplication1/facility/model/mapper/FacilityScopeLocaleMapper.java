package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.CreateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.FacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityScopeLocaleMapper {

    public FacilityScopeLocaleEntity create(CreateFacilityScopeLocaleRequest request,
                                            FacilityScopeEntity facilityScopeEntity,
                                            LocaleEntity localeEntity) {
        FacilityScopeLocaleEntity entity = new FacilityScopeLocaleEntity();
        entity.setFacilityScopeEntity(facilityScopeEntity);
        entity.setLocaleEntity(localeEntity);
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
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
