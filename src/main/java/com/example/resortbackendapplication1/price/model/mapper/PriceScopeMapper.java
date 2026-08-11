package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.price.dto.request.pricescope.CreatePriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.PriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.UpdatePriceScopeRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeDto;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PriceScopeMapper {

    public PriceScopeEntity create(CreatePriceScopeRequest request) {
        PriceScopeEntity entity = new PriceScopeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceScopeEntity entity, UpdatePriceScopeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceScopeEntity entity, PriceScopeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PriceScopeDto.PriceScopeDtoBuilder toDto(PriceScopeEntity entity) {
        return PriceScopeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private PriceScopeLocaleDto singleLocale(PriceScopeEntity entity) {
        PriceScopeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PriceScopeLocaleMapper.toDto(matched);
    }

    private List<PriceScopeLocaleEntity> activeLocales(PriceScopeEntity entity) {
        return entity.getPriceScopeLocaleEntities().stream()
                .filter(priceScopeLocaleEntity -> Boolean.TRUE.equals(priceScopeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(priceScopeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PriceScopeLocaleEntity matchLocale(PriceScopeEntity entity, Long localeId) {
        List<PriceScopeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(priceScopeLocaleEntity -> priceScopeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(priceScopeLocaleEntity -> "en".equals(priceScopeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
