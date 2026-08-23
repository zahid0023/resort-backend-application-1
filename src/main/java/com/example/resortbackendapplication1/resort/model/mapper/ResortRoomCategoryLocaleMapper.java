package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.ResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryLocaleMapper {

    public ResortRoomCategoryLocaleEntity create(ResortRoomCategoryLocaleRequest request) {
        ResortRoomCategoryLocaleEntity entity = new ResortRoomCategoryLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryLocaleEntity entity, UpdateResortRoomCategoryLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryLocaleEntity entity, ResortRoomCategoryLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() == null ? "" : request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomCategoryLocaleDto toDto(ResortRoomCategoryLocaleEntity entity) {
        return ResortRoomCategoryLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
