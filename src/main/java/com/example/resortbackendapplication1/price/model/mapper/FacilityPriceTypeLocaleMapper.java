package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.FacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityPriceTypeLocaleMapper {

    public FacilityPriceTypeLocaleEntity create(FacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeLocaleEntity entity = new FacilityPriceTypeLocaleEntity();
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
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .purpose(entity.getPurpose())
                .usageExample(entity.getUsageExample())
                .build();
    }
}
