package com.example.resortbackendapplication1.resortpermissiontype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.ResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeLocaleDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortPermissionTypeLocaleMapper {

    public ResortPermissionTypeLocaleEntity create(CreateResortPermissionTypeLocaleRequest request,
                                                   ResortPermissionTypeEntity resortPermissionTypeEntity,
                                                   LocaleEntity localeEntity) {
        ResortPermissionTypeLocaleEntity entity = new ResortPermissionTypeLocaleEntity();
        entity.setResortPermissionTypeEntity(resortPermissionTypeEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortPermissionTypeLocaleEntity entity, UpdateResortPermissionTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortPermissionTypeLocaleEntity entity, ResortPermissionTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortPermissionTypeLocaleDto toDto(ResortPermissionTypeLocaleEntity entity) {
        return ResortPermissionTypeLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
