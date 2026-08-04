package com.example.resortbackendapplication1.roomcategory.model.mapper;

import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.locale.RoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.locale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryLocaleDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomCategoryLocaleMapper {

    public RoomCategoryLocaleEntity create(RoomCategoryLocaleRequest request) {
        RoomCategoryLocaleEntity entity = new RoomCategoryLocaleEntity();
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
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
