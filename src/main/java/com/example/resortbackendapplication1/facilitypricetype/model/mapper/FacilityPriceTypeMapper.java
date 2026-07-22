package com.example.resortbackendapplication1.facilitypricetype.model.mapper;

import com.example.resortbackendapplication1.facilitypricetype.dto.request.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.FacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeLocaleDto;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityPriceTypeMapper {

    public FacilityPriceTypeEntity create(CreateFacilityPriceTypeRequest request) {
        FacilityPriceTypeEntity entity = new FacilityPriceTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityPriceTypeEntity entity, UpdateFacilityPriceTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityPriceTypeEntity entity, FacilityPriceTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityPriceTypeDto toDto(FacilityPriceTypeEntity entity) {
        List<FacilityPriceTypeLocaleDto> locales = entity.getFacilityPriceTypeLocaleEntities().stream()
                .filter(l -> Boolean.TRUE.equals(l.getIsActive()) && Boolean.FALSE.equals(l.getIsDeleted()))
                .map(FacilityPriceTypeLocaleMapper::toDto)
                .toList();

        return FacilityPriceTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
