package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortFacilityMapper {

    public ResortFacilityEntity create(CreateResortFacilityRequest request,
                                       FacilityEntity facilityEntity) {
        ResortFacilityEntity entity = new ResortFacilityEntity();
        entity.setFacilityEntity(facilityEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityEntity entity, UpdateResortFacilityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityEntity entity, ResortFacilityRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIsHighlighted(request.getIsHighlighted());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public ResortFacilityDto.ResortFacilityDtoBuilder toDto(ResortFacilityEntity entity) {
        return ResortFacilityDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .isHighlighted(entity.getIsHighlighted())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private ResortFacilityLocaleDto singleLocale(ResortFacilityEntity entity) {
        ResortFacilityLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortFacilityLocaleMapper.toDto(matched);
    }

    private List<ResortFacilityLocaleEntity> activeLocales(ResortFacilityEntity entity) {
        return entity.getResortFacilityLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortFacilityLocaleEntity matchLocale(ResortFacilityEntity entity, Long localeId) {
        List<ResortFacilityLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
