package com.example.resortbackendapplication1.resortaccesstype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.CreateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.ResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.UpdateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.dto.ResortAccessTypeLocaleDto;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortAccessTypeLocaleMapper {

    public ResortAccessTypeLocaleEntity create(CreateResortAccessTypeLocaleRequest request,
                                               ResortAccessTypeEntity resortAccessTypeEntity,
                                               LocaleEntity localeEntity) {
        ResortAccessTypeLocaleEntity entity = new ResortAccessTypeLocaleEntity();
        entity.setResortAccessTypeEntity(resortAccessTypeEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortAccessTypeLocaleEntity entity, UpdateResortAccessTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortAccessTypeLocaleEntity entity, ResortAccessTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortAccessTypeLocaleDto toDto(ResortAccessTypeLocaleEntity entity) {
        return ResortAccessTypeLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
