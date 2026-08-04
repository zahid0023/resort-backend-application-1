package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityGroupMapper {

    public FacilityGroupEntity create(CreateFacilityGroupRequest request) {
        FacilityGroupEntity entity = new FacilityGroupEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityGroupEntity entity, UpdateFacilityGroupRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityGroupEntity entity, FacilityGroupRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public FacilityGroupDto.FacilityGroupDtoBuilder toDto(FacilityGroupEntity entity) {
        return FacilityGroupDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private FacilityGroupLocaleDto singleLocale(FacilityGroupEntity entity) {
        FacilityGroupLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : FacilityGroupLocaleMapper.toDto(matched);
    }

    private List<FacilityGroupLocaleEntity> activeLocales(FacilityGroupEntity entity) {
        return entity.getFacilityGroupLocaleEntities().stream()
                .filter(facilityGroupLocaleEntity -> Boolean.TRUE.equals(facilityGroupLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(facilityGroupLocaleEntity.getIsDeleted()))
                .toList();
    }

    private FacilityGroupLocaleEntity matchLocale(FacilityGroupEntity entity, Long localeId) {
        List<FacilityGroupLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(facilityGroupLocaleEntity -> facilityGroupLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(facilityGroupLocaleEntity -> "en".equals(facilityGroupLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
