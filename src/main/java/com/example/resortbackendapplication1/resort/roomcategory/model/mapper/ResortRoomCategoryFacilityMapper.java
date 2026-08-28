package com.example.resortbackendapplication1.resort.roomcategory.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.CreateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.UpdateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoomCategoryFacilityMapper {

    public ResortRoomCategoryFacilityEntity create(CreateResortRoomCategoryFacilityRequest request,
                                                    FacilityEntity facilityEntity) {
        ResortRoomCategoryFacilityEntity entity = new ResortRoomCategoryFacilityEntity();
        entity.setFacilityEntity(facilityEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryFacilityEntity entity, UpdateResortRoomCategoryFacilityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryFacilityEntity entity, ResortRoomCategoryFacilityRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIsHighlighted(request.getIsHighlighted());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
    }

    public ResortRoomCategoryFacilityDto.ResortRoomCategoryFacilityDtoBuilder toDto(ResortRoomCategoryFacilityEntity entity) {
        return ResortRoomCategoryFacilityDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .isHighlighted(entity.getIsHighlighted())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locale(singleLocale(entity));
    }

    private ResortRoomCategoryFacilityLocaleDto singleLocale(ResortRoomCategoryFacilityEntity entity) {
        ResortRoomCategoryFacilityLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoomCategoryFacilityLocaleMapper.toDto(matched);
    }

    private List<ResortRoomCategoryFacilityLocaleEntity> activeLocales(ResortRoomCategoryFacilityEntity entity) {
        return entity.getResortRoomCategoryFacilityLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoomCategoryFacilityLocaleEntity matchLocale(ResortRoomCategoryFacilityEntity entity, Long localeId) {
        List<ResortRoomCategoryFacilityLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
