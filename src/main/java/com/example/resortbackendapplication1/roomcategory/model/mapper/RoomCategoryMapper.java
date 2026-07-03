package com.example.resortbackendapplication1.roomcategory.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryDto;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryLocaleDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RoomCategoryMapper {

    public RoomCategoryEntity create(CreateRoomCategoryRequest request,
                                     Map<Long, LocaleEntity> localeEntityMap) {
        RoomCategoryEntity entity = new RoomCategoryEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setRoomCategoryLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(RoomCategoryEntity entity, UpdateRoomCategoryRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(RoomCategoryEntity entity, RoomCategoryRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<RoomCategoryLocaleEntity> mapLocales(List<CreateRoomCategoryLocaleRequest> locales,
                                                      RoomCategoryEntity entity,
                                                      Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> RoomCategoryLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public RoomCategoryDto toDto(RoomCategoryEntity entity) {
        List<RoomCategoryLocaleDto> locales = entity.getRoomCategoryLocaleEntities().stream()
                .map(RoomCategoryLocaleMapper::toDto)
                .toList();
        return RoomCategoryDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
