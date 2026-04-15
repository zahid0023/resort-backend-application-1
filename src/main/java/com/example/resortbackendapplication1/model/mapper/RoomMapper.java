package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.rooms.CreateRoomRequest;
import com.example.resortbackendapplication1.dto.request.rooms.UpdateRoomRequest;
import com.example.resortbackendapplication1.model.dto.RoomDto;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomMapper {

    public static RoomEntity fromRequest(CreateRoomRequest request, ResortRoomCategoryEntity resortRoomCategoryEntity) {
        RoomEntity entity = new RoomEntity();
        entity.setResortRoomCategoryEntity(resortRoomCategoryEntity);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setRoomNumber(request.getRoomNumber());
        entity.setFloor(request.getFloor());
        entity.setMaxAdults(request.getMaxAdults());
        int maxChildren = request.getMaxChildren() != null ? request.getMaxChildren() : 0;
        entity.setMaxChildren(maxChildren);
        entity.setMaxOccupancy(request.getMaxAdults() + maxChildren);
        entity.setBasePrice(request.getBasePrice());
        return entity;
    }

    public static void updateEntity(RoomEntity entity, UpdateRoomRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getRoomNumber() != null) entity.setRoomNumber(request.getRoomNumber());
        if (request.getFloor() != null) entity.setFloor(request.getFloor());
        if (request.getBasePrice() != null) entity.setBasePrice(request.getBasePrice());

        Integer maxAdults = request.getMaxAdults() != null ? request.getMaxAdults() : entity.getMaxAdults();
        Integer maxChildren = request.getMaxChildren() != null ? request.getMaxChildren() : entity.getMaxChildren();
        if (request.getMaxAdults() != null) entity.setMaxAdults(maxAdults);
        if (request.getMaxChildren() != null) entity.setMaxChildren(maxChildren);
        if (request.getMaxAdults() != null || request.getMaxChildren() != null) {
            entity.setMaxOccupancy(maxAdults + maxChildren);
        }
    }

    public static RoomDto toDto(RoomEntity entity) {
        return RoomDto.builder()
                .id(entity.getId())
                .resortRoomCategoryId(entity.getResortRoomCategoryEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .roomNumber(entity.getRoomNumber())
                .floor(entity.getFloor())
                .maxAdults(entity.getMaxAdults())
                .maxChildren(entity.getMaxChildren())
                .maxOccupancy(entity.getMaxOccupancy())
                .basePrice(entity.getBasePrice())
                .build();
    }
}
