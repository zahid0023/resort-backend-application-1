package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.PriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceUnitLocaleMapper {

    public PriceUnitLocaleEntity create(CreatePriceUnitLocaleRequest request,
                                        PriceUnitEntity priceUnitEntity,
                                        LocaleEntity localeEntity) {
        PriceUnitLocaleEntity entity = new PriceUnitLocaleEntity();
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setLocaleEntity(localeEntity);
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
        entity.setCalculationMethod(request.getCalculationMethod());
        entity.setUsageExample(request.getUsageExample());
    }

    public PriceUnitLocaleDto toDto(PriceUnitLocaleEntity entity) {
        return PriceUnitLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .calculationMethod(entity.getCalculationMethod())
                .usageExample(entity.getUsageExample())
                .build();
    }
}
