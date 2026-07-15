package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.ResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityLocaleMapper {

    public ResortFacilityLocaleEntity create(CreateResortFacilityLocaleRequest request,
                                             ResortFacilityEntity resortFacilityEntity,
                                             LocaleEntity localeEntity) {
        ResortFacilityLocaleEntity entity = new ResortFacilityLocaleEntity();
        entity.setResortFacilityEntity(resortFacilityEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityLocaleEntity entity, UpdateResortFacilityLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityLocaleEntity entity, ResortFacilityLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortFacilityLocaleDto toDto(ResortFacilityLocaleEntity entity) {
        return ResortFacilityLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
