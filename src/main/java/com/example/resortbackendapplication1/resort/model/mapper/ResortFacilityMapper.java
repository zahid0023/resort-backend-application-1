package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortFacilityMapper {

    public ResortFacilityEntity create(CreateResortFacilityRequest request,
                                       ResortEntity resortEntity,
                                       ResortFacilityGroupEntity resortFacilityGroupEntity,
                                       FacilityEntity facilityEntity,
                                       Map<Long, LocaleEntity> localeEntityMap) {
        ResortFacilityEntity entity = new ResortFacilityEntity();
        entity.setResortEntity(resortEntity);
        entity.setResortFacilityGroupEntity(resortFacilityGroupEntity);
        entity.setFacilityEntity(facilityEntity);
        applyCommonFields(entity, request);
        entity.setResortFacilityLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortFacilityEntity entity,
                       UpdateResortFacilityRequest request,
                       FacilityEntity facilityEntity) {
        entity.setFacilityEntity(facilityEntity);
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityEntity entity, ResortFacilityRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
        entity.setIsHighlighted(request.getIsHighlighted() != null && request.getIsHighlighted());
    }

    private Set<ResortFacilityLocaleEntity> mapLocales(List<CreateResortFacilityLocaleRequest> locales,
                                                        ResortFacilityEntity entity,
                                                        Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ResortFacilityLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public ResortFacilityDto toDto(ResortFacilityEntity entity) {
        List<ResortFacilityLocaleDto> locales = entity.getResortFacilityLocaleEntities().stream()
                .map(ResortFacilityLocaleMapper::toDto)
                .toList();

        return ResortFacilityDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .resortFacilityGroupId(entity.getResortFacilityGroupEntity().getId())
                .facilityId(entity.getFacilityEntity() != null ? entity.getFacilityEntity().getId() : null)
                .sortOrder(entity.getSortOrder())
                .isHighlighted(entity.getIsHighlighted())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locales(locales)
                .build();
    }
}
