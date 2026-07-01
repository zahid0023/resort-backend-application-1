package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.FacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class FacilityMapper {

    public FacilityEntity create(CreateFacilityRequest request,
                                 FacilityGroupEntity facilityGroupEntity,
                                 Map<Long, LocaleEntity> localeEntityMap) {
        FacilityEntity entity = new FacilityEntity();
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setFacilityLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
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

    private Set<FacilityLocaleEntity> mapLocales(List<CreateFacilityLocaleRequest> locales,
                                                  FacilityEntity entity,
                                                  Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> FacilityLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public FacilityDto toDto(FacilityEntity entity) {
        List<FacilityLocaleDto> locales = entity.getFacilityLocaleEntities().stream()
                .map(FacilityLocaleMapper::toDto)
                .toList();

        return FacilityDto.builder()
                .id(entity.getId())
                .facilityGroupId(entity.getFacilityGroupEntity().getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locales(locales)
                .build();
    }
}
