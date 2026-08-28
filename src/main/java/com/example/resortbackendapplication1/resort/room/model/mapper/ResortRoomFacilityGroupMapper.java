package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.CreateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.ResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.UpdateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoomFacilityGroupMapper {

    public ResortRoomFacilityGroupEntity create(CreateResortRoomFacilityGroupRequest request,
                                                 FacilityGroupEntity facilityGroupEntity) {
        ResortRoomFacilityGroupEntity entity = new ResortRoomFacilityGroupEntity();
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomFacilityGroupEntity entity, UpdateResortRoomFacilityGroupRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomFacilityGroupEntity entity, ResortRoomFacilityGroupRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public ResortRoomFacilityGroupDto.ResortRoomFacilityGroupDtoBuilder toDto(ResortRoomFacilityGroupEntity entity) {
        return ResortRoomFacilityGroupDto.builder()
                .id(entity.getId())
                .facilityGroupId(entity.getFacilityGroupEntity() != null ? entity.getFacilityGroupEntity().getId() : null)
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private ResortRoomFacilityGroupLocaleDto singleLocale(ResortRoomFacilityGroupEntity entity) {
        ResortRoomFacilityGroupLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoomFacilityGroupLocaleMapper.toDto(matched);
    }

    private List<ResortRoomFacilityGroupLocaleEntity> activeLocales(ResortRoomFacilityGroupEntity entity) {
        return entity.getResortRoomFacilityGroupLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoomFacilityGroupLocaleEntity matchLocale(ResortRoomFacilityGroupEntity entity, Long localeId) {
        List<ResortRoomFacilityGroupLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
