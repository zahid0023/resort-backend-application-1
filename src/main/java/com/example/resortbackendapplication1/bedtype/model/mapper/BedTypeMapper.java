package com.example.resortbackendapplication1.bedtype.model.mapper;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.CreateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.UpdateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeDto;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeLocaleDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class BedTypeMapper {

    public BedTypeEntity create(CreateBedTypeRequest request,
                                Map<Long, LocaleEntity> localeEntityMap) {
        BedTypeEntity entity = new BedTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setBedTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(BedTypeEntity entity, UpdateBedTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(BedTypeEntity entity, BedTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<BedTypeLocaleEntity> mapLocales(List<CreateBedTypeLocaleRequest> locales,
                                                 BedTypeEntity entity,
                                                 Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> BedTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public BedTypeDto toDto(BedTypeEntity entity) {
        List<BedTypeLocaleDto> locales = entity.getBedTypeLocaleEntities().stream()
                .map(BedTypeLocaleMapper::toDto)
                .toList();

        return BedTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
