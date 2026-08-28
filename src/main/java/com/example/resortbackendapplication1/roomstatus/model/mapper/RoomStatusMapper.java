package com.example.resortbackendapplication1.roomstatus.model.mapper;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.RoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.CreateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.UpdateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusDto;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusLocaleDto;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RoomStatusMapper {

    public RoomStatusEntity create(CreateRoomStatusRequest request) {
        RoomStatusEntity entity = new RoomStatusEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(RoomStatusEntity entity, UpdateRoomStatusRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(RoomStatusEntity entity, RoomStatusRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public RoomStatusDto.RoomStatusDtoBuilder toDto(RoomStatusEntity entity) {
        return RoomStatusDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<RoomStatusLocaleEntity> activeLocales(RoomStatusEntity entity) {
        return entity.getRoomStatusLocaleEntities().stream()
                .filter(roomStatusLocaleEntity -> Boolean.TRUE.equals(roomStatusLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(roomStatusLocaleEntity.getIsDeleted()))
                .toList();
    }

    private RoomStatusLocaleDto singleLocale(RoomStatusEntity entity) {
        RoomStatusLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : RoomStatusLocaleMapper.toDto(matched);
    }

    private RoomStatusLocaleEntity matchLocale(RoomStatusEntity entity, Long localeId) {
        List<RoomStatusLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(roomStatusLocaleEntity -> roomStatusLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(roomStatusLocaleEntity -> "en".equals(roomStatusLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
