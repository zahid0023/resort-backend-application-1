package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.price.dto.request.priceunit.CreatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.UpdatePriceUnitRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PriceUnitMapper {

    public PriceUnitEntity create(CreatePriceUnitRequest request) {
        PriceUnitEntity entity = new PriceUnitEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceUnitEntity entity, UpdatePriceUnitRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceUnitEntity entity, PriceUnitRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PriceUnitDto.PriceUnitDtoBuilder toDto(PriceUnitEntity entity) {
        return PriceUnitDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private PriceUnitLocaleDto singleLocale(PriceUnitEntity entity) {
        PriceUnitLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PriceUnitLocaleMapper.toDto(matched);
    }

    private List<PriceUnitLocaleEntity> activeLocales(PriceUnitEntity entity) {
        return entity.getPriceUnitLocaleEntities().stream()
                .filter(priceUnitLocaleEntity -> Boolean.TRUE.equals(priceUnitLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(priceUnitLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PriceUnitLocaleEntity matchLocale(PriceUnitEntity entity, Long localeId) {
        List<PriceUnitLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(priceUnitLocaleEntity -> priceUnitLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(priceUnitLocaleEntity -> "en".equals(priceUnitLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
