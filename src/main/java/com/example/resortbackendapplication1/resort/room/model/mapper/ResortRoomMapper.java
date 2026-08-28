package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.CreateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoomMapper {

    public ResortRoomEntity create(CreateResortRoomRequest request) {
        ResortRoomEntity entity = new ResortRoomEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomEntity entity, UpdateResortRoomRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomEntity entity, ResortRoomRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setFloorNumber(request.getFloorNumber());
        entity.setBuilding(request.getBuilding());
    }

    public ResortRoomDto.ResortRoomDtoBuilder toDto(ResortRoomEntity entity) {
        return ResortRoomDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .floorNumber(entity.getFloorNumber())
                .building(entity.getBuilding())
                .locale(singleLocale(entity));
    }

    private ResortRoomLocaleDto singleLocale(ResortRoomEntity entity) {
        ResortRoomLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoomLocaleMapper.toDto(matched);
    }

    private List<ResortRoomLocaleEntity> activeLocales(ResortRoomEntity entity) {
        return entity.getResortRoomLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoomLocaleEntity matchLocale(ResortRoomEntity entity, Long localeId) {
        List<ResortRoomLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
