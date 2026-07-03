package com.example.resortbackendapplication1.uiblocksection.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.UiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.UpdateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionLocaleDto;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UiBlockSectionLocaleMapper {

    public UiBlockSectionLocaleEntity create(CreateUiBlockSectionLocaleRequest request,
                                             UiBlockSectionEntity uiBlockSectionEntity,
                                             LocaleEntity localeEntity) {
        UiBlockSectionLocaleEntity entity = new UiBlockSectionLocaleEntity();
        entity.setUiBlockSectionEntity(uiBlockSectionEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(UiBlockSectionLocaleEntity entity, UpdateUiBlockSectionLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(UiBlockSectionLocaleEntity entity, UiBlockSectionLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public UiBlockSectionLocaleDto toDto(UiBlockSectionLocaleEntity entity) {
        return UiBlockSectionLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
