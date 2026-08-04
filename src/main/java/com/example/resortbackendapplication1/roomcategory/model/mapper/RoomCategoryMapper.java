package com.example.resortbackendapplication1.roomcategory.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryDto;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryLocaleDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RoomCategoryMapper {

    public RoomCategoryEntity create(CreateRoomCategoryRequest request) {
        RoomCategoryEntity entity = new RoomCategoryEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(RoomCategoryEntity entity, UpdateRoomCategoryRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(RoomCategoryEntity entity, RoomCategoryRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public RoomCategoryDto.RoomCategoryDtoBuilder toDto(RoomCategoryEntity entity) {
        return RoomCategoryDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private RoomCategoryLocaleDto singleLocale(RoomCategoryEntity entity) {
        RoomCategoryLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : RoomCategoryLocaleMapper.toDto(matched);
    }

    private List<RoomCategoryLocaleEntity> activeLocales(RoomCategoryEntity entity) {
        return entity.getRoomCategoryLocaleEntities().stream()
                .filter(roomCategoryLocaleEntity -> Boolean.TRUE.equals(roomCategoryLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(roomCategoryLocaleEntity.getIsDeleted()))
                .toList();
    }

    private RoomCategoryLocaleEntity matchLocale(RoomCategoryEntity entity, Long localeId) {
        List<RoomCategoryLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(roomCategoryLocaleEntity -> roomCategoryLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(roomCategoryLocaleEntity -> "en".equals(roomCategoryLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
