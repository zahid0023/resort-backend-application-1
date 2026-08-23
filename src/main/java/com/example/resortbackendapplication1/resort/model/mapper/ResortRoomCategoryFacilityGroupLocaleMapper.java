package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.ResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.UpdateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryFacilityGroupLocaleMapper {

    public ResortRoomCategoryFacilityGroupLocaleEntity create(ResortRoomCategoryFacilityGroupLocaleRequest request) {
        ResortRoomCategoryFacilityGroupLocaleEntity entity = new ResortRoomCategoryFacilityGroupLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryFacilityGroupLocaleEntity entity, UpdateResortRoomCategoryFacilityGroupLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryFacilityGroupLocaleEntity entity, ResortRoomCategoryFacilityGroupLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomCategoryFacilityGroupLocaleDto toDto(ResortRoomCategoryFacilityGroupLocaleEntity entity) {
        return ResortRoomCategoryFacilityGroupLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
