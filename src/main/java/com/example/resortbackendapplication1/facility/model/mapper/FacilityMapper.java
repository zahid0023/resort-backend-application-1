package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.dto.request.facility.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.FacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityMapper {

    public FacilityEntity create(CreateFacilityRequest request) {
        FacilityEntity entity = new FacilityEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityEntity entity, UpdateFacilityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityEntity entity, FacilityRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public FacilityDto.FacilityDtoBuilder toDto(FacilityEntity entity) {
        return FacilityDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private FacilityLocaleDto singleLocale(FacilityEntity entity) {
        FacilityLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : FacilityLocaleMapper.toDto(matched);
    }

    private List<FacilityLocaleEntity> activeLocales(FacilityEntity entity) {
        return entity.getFacilityLocaleEntities().stream()
                .filter(facilityLocaleEntity -> Boolean.TRUE.equals(facilityLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(facilityLocaleEntity.getIsDeleted()))
                .toList();
    }

    private FacilityLocaleEntity matchLocale(FacilityEntity entity, Long localeId) {
        List<FacilityLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(facilityLocaleEntity -> facilityLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(facilityLocaleEntity -> "en".equals(facilityLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
