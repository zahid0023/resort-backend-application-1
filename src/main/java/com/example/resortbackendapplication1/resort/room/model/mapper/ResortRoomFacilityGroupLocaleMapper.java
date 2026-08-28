package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.ResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.UpdateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomFacilityGroupLocaleMapper {

    public ResortRoomFacilityGroupLocaleEntity create(ResortRoomFacilityGroupLocaleRequest request) {
        ResortRoomFacilityGroupLocaleEntity entity = new ResortRoomFacilityGroupLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomFacilityGroupLocaleEntity entity, UpdateResortRoomFacilityGroupLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomFacilityGroupLocaleEntity entity, ResortRoomFacilityGroupLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomFacilityGroupLocaleDto toDto(ResortRoomFacilityGroupLocaleEntity entity) {
        return ResortRoomFacilityGroupLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
