package com.example.resortbackendapplication1.resortroomcategory.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.ResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryLocaleMapper {

    public ResortRoomCategoryLocaleEntity create(CreateResortRoomCategoryLocaleRequest request,
                                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                  LocaleEntity localeEntity) {
        ResortRoomCategoryLocaleEntity entity = new ResortRoomCategoryLocaleEntity();
        entity.setResortRoomCategoryEntity(resortRoomCategoryEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryLocaleEntity entity, UpdateResortRoomCategoryLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryLocaleEntity entity, ResortRoomCategoryLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomCategoryLocaleDto toDto(ResortRoomCategoryLocaleEntity entity) {
        return ResortRoomCategoryLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
