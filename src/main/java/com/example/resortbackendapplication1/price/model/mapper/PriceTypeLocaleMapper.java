package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.PriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceTypeLocaleMapper {

    public PriceTypeLocaleEntity create(CreatePriceTypeLocaleRequest request,
                                        PriceTypeEntity priceTypeEntity,
                                        LocaleEntity localeEntity) {
        PriceTypeLocaleEntity entity = new PriceTypeLocaleEntity();
        entity.setPriceTypeEntity(priceTypeEntity);
        entity.setLocaleEntity(localeEntity);
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
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .purpose(entity.getPurpose())
                .usageExample(entity.getUsageExample())
                .build();
    }
}
