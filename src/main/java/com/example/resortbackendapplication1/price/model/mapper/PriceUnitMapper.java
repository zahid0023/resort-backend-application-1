package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.priceunit.CreatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.UpdatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PriceUnitMapper {

    public PriceUnitEntity create(CreatePriceUnitRequest request,
                                  Map<Long, LocaleEntity> localeEntityMap) {
        PriceUnitEntity entity = new PriceUnitEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setPriceUnitLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(PriceUnitEntity entity, UpdatePriceUnitRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceUnitEntity entity, PriceUnitRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<PriceUnitLocaleEntity> mapLocales(List<CreatePriceUnitLocaleRequest> locales,
                                                   PriceUnitEntity entity,
                                                   Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> PriceUnitLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public PriceUnitDto toDto(PriceUnitEntity entity) {
        List<PriceUnitLocaleDto> locales = entity.getPriceUnitLocaleEntities().stream()
                .map(PriceUnitLocaleMapper::toDto)
                .toList();

        return PriceUnitDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
