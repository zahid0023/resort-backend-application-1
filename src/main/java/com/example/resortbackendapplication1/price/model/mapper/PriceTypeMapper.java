package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PriceTypeMapper {

    public PriceTypeEntity create(CreatePriceTypeRequest request) {
        PriceTypeEntity entity = new PriceTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceTypeEntity entity, UpdatePriceTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceTypeEntity entity, PriceTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PriceTypeDto.PriceTypeDtoBuilder toDto(PriceTypeEntity entity) {
        return PriceTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private PriceTypeLocaleDto singleLocale(PriceTypeEntity entity) {
        PriceTypeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PriceTypeLocaleMapper.toDto(matched);
    }

    private List<PriceTypeLocaleEntity> activeLocales(PriceTypeEntity entity) {
        return entity.getPriceTypeLocaleEntities().stream()
                .filter(priceTypeLocaleEntity -> Boolean.TRUE.equals(priceTypeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(priceTypeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PriceTypeLocaleEntity matchLocale(PriceTypeEntity entity, Long localeId) {
        List<PriceTypeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(priceTypeLocaleEntity -> priceTypeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(priceTypeLocaleEntity -> "en".equals(priceTypeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
