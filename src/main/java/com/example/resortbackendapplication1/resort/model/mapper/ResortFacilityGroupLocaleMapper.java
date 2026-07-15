package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.ResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityGroupLocaleMapper {

    public ResortFacilityGroupLocaleEntity create(CreateResortFacilityGroupLocaleRequest request,
                                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                                  LocaleEntity localeEntity) {
        ResortFacilityGroupLocaleEntity entity = new ResortFacilityGroupLocaleEntity();
        entity.setResortFacilityGroupEntity(resortFacilityGroupEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityGroupLocaleEntity entity, UpdateResortFacilityGroupLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityGroupLocaleEntity entity, ResortFacilityGroupLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortFacilityGroupLocaleDto toDto(ResortFacilityGroupLocaleEntity entity) {
        return ResortFacilityGroupLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
