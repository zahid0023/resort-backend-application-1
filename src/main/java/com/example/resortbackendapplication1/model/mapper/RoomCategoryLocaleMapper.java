package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.RoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.model.dto.RoomCategoryLocaleDto;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomCategoryLocaleMapper {

    public RoomCategoryLocaleEntity create(CreateRoomCategoryLocaleRequest request,
                                           RoomCategoryEntity roomCategoryEntity,
                                           LocaleEntity localeEntity) {
        RoomCategoryLocaleEntity entity = new RoomCategoryLocaleEntity();
        entity.setRoomCategoryEntity(roomCategoryEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(RoomCategoryLocaleEntity entity, UpdateRoomCategoryLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(RoomCategoryLocaleEntity entity, RoomCategoryLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public RoomCategoryLocaleDto toDto(RoomCategoryLocaleEntity entity) {
        return RoomCategoryLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
