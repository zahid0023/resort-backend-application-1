package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.PriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceTypeLocaleMapper {

    public PriceTypeLocaleEntity create(PriceTypeLocaleRequest request) {
        PriceTypeLocaleEntity entity = new PriceTypeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceTypeLocaleEntity entity, UpdatePriceTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceTypeLocaleEntity entity, PriceTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        entity.setPurpose(request.getPurpose());
        entity.setUsageExample(request.getUsageExample());
    }

    public PriceTypeLocaleDto toDto(PriceTypeLocaleEntity entity) {
        return PriceTypeLocaleDto.builder()
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
