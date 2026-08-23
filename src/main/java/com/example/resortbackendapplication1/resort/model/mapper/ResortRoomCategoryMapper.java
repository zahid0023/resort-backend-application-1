package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.ResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoomCategoryMapper {

    public ResortRoomCategoryEntity create(CreateResortRoomCategoryRequest request) {
        ResortRoomCategoryEntity entity = new ResortRoomCategoryEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryEntity entity, ResortRoomCategoryRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoomCategoryDto.ResortRoomCategoryDtoBuilder toDto(ResortRoomCategoryEntity entity) {
        return ResortRoomCategoryDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private ResortRoomCategoryLocaleDto singleLocale(ResortRoomCategoryEntity entity) {
        ResortRoomCategoryLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoomCategoryLocaleMapper.toDto(matched);
    }

    private List<ResortRoomCategoryLocaleEntity> activeLocales(ResortRoomCategoryEntity entity) {
        return entity.getResortRoomCategoryLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoomCategoryLocaleEntity matchLocale(ResortRoomCategoryEntity entity, Long localeId) {
        List<ResortRoomCategoryLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
