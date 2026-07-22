package com.example.resortbackendapplication1.unittype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.CreateUnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UpdateUnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.CreateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.model.dto.UnitTypeDto;
import com.example.resortbackendapplication1.unittype.model.dto.UnitTypeLocaleDto;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UnitTypeMapper {

    public UnitTypeEntity create(CreateUnitTypeRequest request,
                                 Map<Long, LocaleEntity> localeEntityMap) {
        UnitTypeEntity entity = new UnitTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setUnitTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(UnitTypeEntity entity, UpdateUnitTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(UnitTypeEntity entity, UnitTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<UnitTypeLocaleEntity> mapLocales(List<CreateUnitTypeLocaleRequest> locales,
                                                  UnitTypeEntity entity,
                                                  Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> UnitTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public UnitTypeDto toDto(UnitTypeEntity entity) {
        List<UnitTypeLocaleDto> locales = entity.getUnitTypeLocaleEntities().stream()
                .map(UnitTypeLocaleMapper::toDto)
                .toList();

        return UnitTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
