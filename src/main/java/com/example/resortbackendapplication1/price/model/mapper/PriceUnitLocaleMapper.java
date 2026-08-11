package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.PriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceUnitLocaleMapper {

    public PriceUnitLocaleEntity create(PriceUnitLocaleRequest request) {
        PriceUnitLocaleEntity entity = new PriceUnitLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceUnitLocaleEntity entity, UpdatePriceUnitLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceUnitLocaleEntity entity, PriceUnitLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        entity.setPurpose(request.getPurpose());
        entity.setUsageExample(request.getUsageExample());
    }

    public PriceUnitLocaleDto toDto(PriceUnitLocaleEntity entity) {
        return PriceUnitLocaleDto.builder()
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
