package com.example.resortbackendapplication1.facilitypricetype.model.mapper;

import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.FacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeLocaleDto;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityPriceTypeLocaleMapper {

    public FacilityPriceTypeLocaleEntity create(CreateFacilityPriceTypeLocaleRequest request,
                                                FacilityPriceTypeEntity facilityPriceTypeEntity,
                                                LocaleEntity localeEntity) {
        FacilityPriceTypeLocaleEntity entity = new FacilityPriceTypeLocaleEntity();
        entity.setFacilityPriceTypeEntity(facilityPriceTypeEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityPriceTypeLocaleEntity entity, UpdateFacilityPriceTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityPriceTypeLocaleEntity entity, FacilityPriceTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        entity.setPurpose(request.getPurpose());
        entity.setUsageExample(request.getUsageExample());
    }

    public FacilityPriceTypeLocaleDto toDto(FacilityPriceTypeLocaleEntity entity) {
        return FacilityPriceTypeLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .purpose(entity.getPurpose())
                .usageExample(entity.getUsageExample())
                .build();
    }
}
