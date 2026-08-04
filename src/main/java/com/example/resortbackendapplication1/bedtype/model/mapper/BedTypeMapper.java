package com.example.resortbackendapplication1.bedtype.model.mapper;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.CreateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.UpdateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeDto;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeLocaleDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BedTypeMapper {

    public BedTypeEntity create(CreateBedTypeRequest request) {
        BedTypeEntity entity = new BedTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(BedTypeEntity entity, UpdateBedTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(BedTypeEntity entity, BedTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public BedTypeDto.BedTypeDtoBuilder toDto(BedTypeEntity entity) {
        return BedTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<BedTypeLocaleEntity> activeLocales(BedTypeEntity entity) {
        return entity.getBedTypeLocaleEntities().stream()
                .filter(bedTypeLocaleEntity -> Boolean.TRUE.equals(bedTypeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(bedTypeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private BedTypeLocaleDto singleLocale(BedTypeEntity entity) {
        BedTypeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : BedTypeLocaleMapper.toDto(matched);
    }

    private BedTypeLocaleEntity matchLocale(BedTypeEntity entity, Long localeId) {
        List<BedTypeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(bedTypeLocaleEntity -> bedTypeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(bedTypeLocaleEntity -> "en".equals(bedTypeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
