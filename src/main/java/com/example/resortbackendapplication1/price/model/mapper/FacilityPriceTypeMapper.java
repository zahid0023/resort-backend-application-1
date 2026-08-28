package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.FacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityPriceTypeMapper {

    public FacilityPriceTypeEntity create(CreateFacilityPriceTypeRequest request) {
        FacilityPriceTypeEntity entity = new FacilityPriceTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityPriceTypeEntity entity, UpdateFacilityPriceTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityPriceTypeEntity entity, FacilityPriceTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityPriceTypeDto.FacilityPriceTypeDtoBuilder toDto(FacilityPriceTypeEntity entity) {
        return FacilityPriceTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private FacilityPriceTypeLocaleDto singleLocale(FacilityPriceTypeEntity entity) {
        FacilityPriceTypeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : FacilityPriceTypeLocaleMapper.toDto(matched);
    }

    private List<FacilityPriceTypeLocaleEntity> activeLocales(FacilityPriceTypeEntity entity) {
        return entity.getFacilityPriceTypeLocaleEntities().stream()
                .filter(facilityPriceTypeLocaleEntity -> Boolean.TRUE.equals(facilityPriceTypeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(facilityPriceTypeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private FacilityPriceTypeLocaleEntity matchLocale(FacilityPriceTypeEntity entity, Long localeId) {
        List<FacilityPriceTypeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(facilityPriceTypeLocaleEntity -> facilityPriceTypeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(facilityPriceTypeLocaleEntity -> "en".equals(facilityPriceTypeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
