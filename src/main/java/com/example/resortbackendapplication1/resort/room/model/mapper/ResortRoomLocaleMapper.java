package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.ResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.UpdateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomLocaleMapper {

    public ResortRoomLocaleEntity create(ResortRoomLocaleRequest request) {
        ResortRoomLocaleEntity entity = new ResortRoomLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomLocaleEntity entity, UpdateResortRoomLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomLocaleEntity entity, ResortRoomLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomLocaleDto toDto(ResortRoomLocaleEntity entity) {
        return ResortRoomLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
