package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.resortroomcategories.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryMapper {

    public static ResortRoomCategoryEntity fromRequest(CreateResortRoomCategoryRequest request,
                                                       ResortEntity resort,
                                                       RoomCategoryEntity roomCategory) {
        ResortRoomCategoryEntity entity = new ResortRoomCategoryEntity();
        entity.setResort(resort);
        entity.setRoomCategory(roomCategory);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        return entity;
    }

    public static void updateEntity(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
    }

    public static ResortRoomCategoryDto toDto(ResortRoomCategoryEntity entity) {
        return ResortRoomCategoryDto.builder()
                .id(entity.getId())
                .resortId(entity.getResort().getId())
                .roomCategoryId(entity.getRoomCategory().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
