package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.FacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityGroupLocaleMapper {

    public FacilityGroupLocaleEntity create(CreateFacilityGroupLocaleRequest request,
                                            FacilityGroupEntity facilityGroupEntity,
                                            LocaleEntity localeEntity) {
        FacilityGroupLocaleEntity entity = new FacilityGroupLocaleEntity();
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setLocaleEntity(localeEntity);
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
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
