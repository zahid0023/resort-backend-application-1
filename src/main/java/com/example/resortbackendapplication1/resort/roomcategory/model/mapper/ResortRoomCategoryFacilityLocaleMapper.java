package com.example.resortbackendapplication1.resort.roomcategory.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.locale.ResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.locale.UpdateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryFacilityLocaleMapper {

    public ResortRoomCategoryFacilityLocaleEntity create(ResortRoomCategoryFacilityLocaleRequest request) {
        ResortRoomCategoryFacilityLocaleEntity entity = new ResortRoomCategoryFacilityLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryFacilityLocaleEntity entity, UpdateResortRoomCategoryFacilityLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryFacilityLocaleEntity entity, ResortRoomCategoryFacilityLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setNotes(request.getNotes() == null ? "" : request.getNotes());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomCategoryFacilityLocaleDto toDto(ResortRoomCategoryFacilityLocaleEntity entity) {
        return ResortRoomCategoryFacilityLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .notes(entity.getNotes())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
