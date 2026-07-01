package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.FacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class FacilityGroupMapper {

    public FacilityGroupEntity create(CreateFacilityGroupRequest request,
                                      Map<Long, LocaleEntity> localeEntityMap) {
        FacilityGroupEntity entity = new FacilityGroupEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setFacilityGroupLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
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

    private Set<FacilityGroupLocaleEntity> mapLocales(List<CreateFacilityGroupLocaleRequest> locales,
                                                       FacilityGroupEntity entity,
                                                       Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new java.util.LinkedHashSet<>();
        return locales.stream()
                .map(locale -> FacilityGroupLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public FacilityGroupDto toDto(FacilityGroupEntity entity) {
        List<FacilityGroupLocaleDto> locales = entity.getFacilityGroupLocaleEntities().stream()
                .map(FacilityGroupLocaleMapper::toDto)
                .toList();

        return FacilityGroupDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locales(locales)
                .build();
    }
}
