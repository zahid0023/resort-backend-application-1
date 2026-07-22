package com.example.resortbackendapplication1.unittype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.CreateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.UnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.UpdateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.model.dto.UnitTypeLocaleDto;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UnitTypeLocaleMapper {

    public UnitTypeLocaleEntity create(CreateUnitTypeLocaleRequest request,
                                       UnitTypeEntity unitTypeEntity,
                                       LocaleEntity localeEntity) {
        UnitTypeLocaleEntity entity = new UnitTypeLocaleEntity();
        entity.setUnitTypeEntity(unitTypeEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(UnitTypeLocaleEntity entity, UpdateUnitTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(UnitTypeLocaleEntity entity, UnitTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public UnitTypeLocaleDto toDto(UnitTypeLocaleEntity entity) {
        return UnitTypeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
