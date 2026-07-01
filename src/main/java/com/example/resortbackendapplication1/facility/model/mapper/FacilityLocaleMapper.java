package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.FacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityLocaleMapper {

    public FacilityLocaleEntity create(CreateFacilityLocaleRequest request,
                                       FacilityEntity facilityEntity,
                                       LocaleEntity localeEntity) {
        FacilityLocaleEntity entity = new FacilityLocaleEntity();
        entity.setFacilityEntity(facilityEntity);
        entity.setLocaleEntity(localeEntity);
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
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
