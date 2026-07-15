package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortFacilityGroupMapper {

    public ResortFacilityGroupEntity create(CreateResortFacilityGroupRequest request,
                                            ResortEntity resortEntity,
                                            FacilityGroupEntity facilityGroupEntity,
                                            Map<Long, LocaleEntity> localeEntityMap) {
        ResortFacilityGroupEntity entity = new ResortFacilityGroupEntity();
        entity.setResortEntity(resortEntity);
        entity.setFacilityGroupEntity(facilityGroupEntity);
        applyCommonFields(entity, request);
        entity.setResortFacilityGroupLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortFacilityGroupEntity entity,
                       UpdateResortFacilityGroupRequest request,
                       FacilityGroupEntity facilityGroupEntity) {
        entity.setFacilityGroupEntity(facilityGroupEntity);
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityGroupEntity entity, ResortFacilityGroupRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    private Set<ResortFacilityGroupLocaleEntity> mapLocales(List<CreateResortFacilityGroupLocaleRequest> locales,
                                                             ResortFacilityGroupEntity entity,
                                                             Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ResortFacilityGroupLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public ResortFacilityGroupDto toDto(ResortFacilityGroupEntity entity) {
        List<ResortFacilityGroupLocaleDto> locales = entity.getResortFacilityGroupLocaleEntities().stream()
                .map(ResortFacilityGroupLocaleMapper::toDto)
                .toList();

        return ResortFacilityGroupDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .facilityGroupId(entity.getFacilityGroupEntity() != null ? entity.getFacilityGroupEntity().getId() : null)
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locales(locales)
                .build();
    }
}
