package com.example.resortbackendapplication1.roomstatus.model.mapper;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.RoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.UpdateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusLocaleDto;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomStatusLocaleMapper {

    public RoomStatusLocaleEntity create(RoomStatusLocaleRequest request) {
        RoomStatusLocaleEntity entity = new RoomStatusLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(RoomStatusLocaleEntity entity, UpdateRoomStatusLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(RoomStatusLocaleEntity entity, RoomStatusLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public RoomStatusLocaleDto toDto(RoomStatusLocaleEntity entity) {
        return RoomStatusLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
