package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.ResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.UpdateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomFacilityLocaleMapper {

    public ResortRoomFacilityLocaleEntity create(ResortRoomFacilityLocaleRequest request) {
        ResortRoomFacilityLocaleEntity entity = new ResortRoomFacilityLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomFacilityLocaleEntity entity, UpdateResortRoomFacilityLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomFacilityLocaleEntity entity, ResortRoomFacilityLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setNotes(request.getNotes() == null ? "" : request.getNotes());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomFacilityLocaleDto toDto(ResortRoomFacilityLocaleEntity entity) {
        return ResortRoomFacilityLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .notes(entity.getNotes())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
