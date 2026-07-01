package com.example.resortbackendapplication1.locale.model.mapper;

import com.example.resortbackendapplication1.locale.dto.request.locale.CreateLocaleRequest;
import com.example.resortbackendapplication1.locale.dto.request.locale.UpdateLocaleRequest;
import com.example.resortbackendapplication1.locale.model.dto.LocaleDto;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocaleMapper {

    public static LocaleEntity fromRequest(CreateLocaleRequest request) {
        LocaleEntity entity = new LocaleEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSortOrder(request.getSortOrder());
        return entity;
    }

    public static void update(LocaleEntity entity, UpdateLocaleRequest request) {
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSortOrder(request.getSortOrder());
    }

    public static LocaleDto toDto(LocaleEntity entity) {
        return LocaleDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
