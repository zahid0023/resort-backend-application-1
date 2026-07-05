package com.example.resortbackendapplication1.resortaccesstype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.CreateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.ResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.UpdateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.CreateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.dto.ResortAccessTypeDto;
import com.example.resortbackendapplication1.resortaccesstype.model.dto.ResortAccessTypeLocaleDto;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortAccessTypeMapper {

    public ResortAccessTypeEntity create(CreateResortAccessTypeRequest request,
                                         Map<Long, LocaleEntity> localeEntityMap) {
        ResortAccessTypeEntity entity = new ResortAccessTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setResortAccessTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortAccessTypeEntity entity, UpdateResortAccessTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortAccessTypeEntity entity, ResortAccessTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<ResortAccessTypeLocaleEntity> mapLocales(List<CreateResortAccessTypeLocaleRequest> locales,
                                                          ResortAccessTypeEntity entity,
                                                          Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ResortAccessTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResortAccessTypeDto toDto(ResortAccessTypeEntity entity) {
        List<ResortAccessTypeLocaleDto> locales = entity.getResortAccessTypeLocaleEntities().stream()
                .map(ResortAccessTypeLocaleMapper::toDto)
                .toList();

        return ResortAccessTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
