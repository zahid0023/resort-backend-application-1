package com.example.resortbackendapplication1.resort.roomcategory.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.CreateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.UpdateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityGroupDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoomCategoryFacilityGroupMapper {

    public ResortRoomCategoryFacilityGroupEntity create(CreateResortRoomCategoryFacilityGroupRequest request,
                                                         FacilityGroupEntity facilityGroupEntity) {
        ResortRoomCategoryFacilityGroupEntity entity = new ResortRoomCategoryFacilityGroupEntity();
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryFacilityGroupEntity entity, UpdateResortRoomCategoryFacilityGroupRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryFacilityGroupEntity entity, ResortRoomCategoryFacilityGroupRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public ResortRoomCategoryFacilityGroupDto.ResortRoomCategoryFacilityGroupDtoBuilder toDto(ResortRoomCategoryFacilityGroupEntity entity) {
        return ResortRoomCategoryFacilityGroupDto.builder()
                .id(entity.getId())
                .facilityGroupId(entity.getFacilityGroupEntity() != null ? entity.getFacilityGroupEntity().getId() : null)
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private ResortRoomCategoryFacilityGroupLocaleDto singleLocale(ResortRoomCategoryFacilityGroupEntity entity) {
        ResortRoomCategoryFacilityGroupLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoomCategoryFacilityGroupLocaleMapper.toDto(matched);
    }

    private List<ResortRoomCategoryFacilityGroupLocaleEntity> activeLocales(ResortRoomCategoryFacilityGroupEntity entity) {
        return entity.getResortRoomCategoryFacilityGroupLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoomCategoryFacilityGroupLocaleEntity matchLocale(ResortRoomCategoryFacilityGroupEntity entity, Long localeId) {
        List<ResortRoomCategoryFacilityGroupLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
