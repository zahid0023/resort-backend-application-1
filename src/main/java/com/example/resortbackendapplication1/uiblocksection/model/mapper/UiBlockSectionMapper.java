package com.example.resortbackendapplication1.uiblocksection.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.CreateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UpdateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionDto;
import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionLocaleDto;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UiBlockSectionMapper {

    public UiBlockSectionEntity create(CreateUiBlockSectionRequest request,
                                       Map<Long, LocaleEntity> localeEntityMap) {
        UiBlockSectionEntity entity = new UiBlockSectionEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setUiBlockSectionLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(UiBlockSectionEntity entity, UpdateUiBlockSectionRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(UiBlockSectionEntity entity, UiBlockSectionRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<UiBlockSectionLocaleEntity> mapLocales(List<CreateUiBlockSectionLocaleRequest> locales,
                                                        UiBlockSectionEntity entity,
                                                        Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> UiBlockSectionLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public UiBlockSectionDto toDto(UiBlockSectionEntity entity) {
        List<UiBlockSectionLocaleDto> locales = entity.getUiBlockSectionLocaleEntities().stream()
                .map(UiBlockSectionLocaleMapper::toDto)
                .toList();

        return UiBlockSectionDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
