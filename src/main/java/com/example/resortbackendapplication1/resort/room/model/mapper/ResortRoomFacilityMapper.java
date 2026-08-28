package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.CreateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.ResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.UpdateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoomFacilityMapper {

    public ResortRoomFacilityEntity create(CreateResortRoomFacilityRequest request,
                                           FacilityEntity facilityEntity) {
        ResortRoomFacilityEntity entity = new ResortRoomFacilityEntity();
        entity.setFacilityEntity(facilityEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomFacilityEntity entity, UpdateResortRoomFacilityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomFacilityEntity entity, ResortRoomFacilityRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIsHighlighted(request.getIsHighlighted());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public ResortRoomFacilityDto.ResortRoomFacilityDtoBuilder toDto(ResortRoomFacilityEntity entity) {
        return ResortRoomFacilityDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .isHighlighted(entity.getIsHighlighted())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private ResortRoomFacilityLocaleDto singleLocale(ResortRoomFacilityEntity entity) {
        ResortRoomFacilityLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoomFacilityLocaleMapper.toDto(matched);
    }

    private List<ResortRoomFacilityLocaleEntity> activeLocales(ResortRoomFacilityEntity entity) {
        return entity.getResortRoomFacilityLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoomFacilityLocaleEntity matchLocale(ResortRoomFacilityEntity entity, Long localeId) {
        List<ResortRoomFacilityLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
