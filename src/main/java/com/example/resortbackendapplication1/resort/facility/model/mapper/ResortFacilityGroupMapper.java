package com.example.resortbackendapplication1.resort.facility.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.ResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortFacilityGroupMapper {

    public ResortFacilityGroupEntity create(CreateResortFacilityGroupRequest request,
                                            ResortEntity resortEntity,
                                            FacilityGroupEntity facilityGroupEntity) {
        ResortFacilityGroupEntity entity = new ResortFacilityGroupEntity();
        entity.setResortEntity(resortEntity);
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityGroupEntity entity, UpdateResortFacilityGroupRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityGroupEntity entity, ResortFacilityGroupRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public ResortFacilityGroupDto.ResortFacilityGroupDtoBuilder toDto(ResortFacilityGroupEntity entity) {
        return ResortFacilityGroupDto.builder()
                .id(entity.getId())
                .facilityGroupId(entity.getFacilityGroupEntity() != null ? entity.getFacilityGroupEntity().getId() : null)
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private ResortFacilityGroupLocaleDto singleLocale(ResortFacilityGroupEntity entity) {
        ResortFacilityGroupLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortFacilityGroupLocaleMapper.toDto(matched);
    }

    private List<ResortFacilityGroupLocaleEntity> activeLocales(ResortFacilityGroupEntity entity) {
        return entity.getResortFacilityGroupLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortFacilityGroupLocaleEntity matchLocale(ResortFacilityGroupEntity entity, Long localeId) {
        List<ResortFacilityGroupLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
