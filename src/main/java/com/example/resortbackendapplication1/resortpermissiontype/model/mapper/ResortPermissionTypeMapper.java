package com.example.resortbackendapplication1.resortpermissiontype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.ResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeLocaleDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortPermissionTypeMapper {

    public ResortPermissionTypeEntity create(CreateResortPermissionTypeRequest request,
                                             Map<Long, LocaleEntity> localeEntityMap) {
        ResortPermissionTypeEntity entity = new ResortPermissionTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setResortPermissionTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortPermissionTypeEntity entity, UpdateResortPermissionTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortPermissionTypeEntity entity, ResortPermissionTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<ResortPermissionTypeLocaleEntity> mapLocales(List<CreateResortPermissionTypeLocaleRequest> locales,
                                                              ResortPermissionTypeEntity entity,
                                                              Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ResortPermissionTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResortPermissionTypeDto toDto(ResortPermissionTypeEntity entity) {
        List<ResortPermissionTypeLocaleDto> locales = entity.getResortPermissionTypeLocaleEntities().stream()
                .map(ResortPermissionTypeLocaleMapper::toDto)
                .toList();

        return ResortPermissionTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
