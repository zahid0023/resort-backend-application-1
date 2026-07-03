package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PriceTypeMapper {

    public PriceTypeEntity create(CreatePriceTypeRequest request,
                                  Map<Long, LocaleEntity> localeEntityMap) {
        PriceTypeEntity entity = new PriceTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setPriceTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(PriceTypeEntity entity, UpdatePriceTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceTypeEntity entity, PriceTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<PriceTypeLocaleEntity> mapLocales(List<CreatePriceTypeLocaleRequest> locales,
                                                   PriceTypeEntity entity,
                                                   Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> PriceTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public PriceTypeDto toDto(PriceTypeEntity entity) {
        List<PriceTypeLocaleDto> locales = entity.getPriceTypeLocaleEntities().stream()
                .map(PriceTypeLocaleMapper::toDto)
                .toList();

        return PriceTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
