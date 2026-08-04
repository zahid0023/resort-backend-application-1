package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.CreatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.PriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.UpdatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeDto;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PriceTypeScopeMapper {

    public PriceTypeScopeEntity create(CreatePriceTypeScopeRequest request) {
        PriceTypeScopeEntity entity = new PriceTypeScopeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceTypeScopeEntity entity, UpdatePriceTypeScopeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceTypeScopeEntity entity, PriceTypeScopeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PriceTypeScopeDto.PriceTypeScopeDtoBuilder toDto(PriceTypeScopeEntity entity) {
        return PriceTypeScopeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private PriceTypeScopeLocaleDto singleLocale(PriceTypeScopeEntity entity) {
        PriceTypeScopeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PriceTypeScopeLocaleMapper.toDto(matched);
    }

    private List<PriceTypeScopeLocaleEntity> activeLocales(PriceTypeScopeEntity entity) {
        return entity.getPriceTypeScopeLocaleEntities().stream()
                .filter(priceTypeScopeLocaleEntity -> Boolean.TRUE.equals(priceTypeScopeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(priceTypeScopeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PriceTypeScopeLocaleEntity matchLocale(PriceTypeScopeEntity entity, Long localeId) {
        List<PriceTypeScopeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(priceTypeScopeLocaleEntity -> priceTypeScopeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(priceTypeScopeLocaleEntity -> "en".equals(priceTypeScopeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
