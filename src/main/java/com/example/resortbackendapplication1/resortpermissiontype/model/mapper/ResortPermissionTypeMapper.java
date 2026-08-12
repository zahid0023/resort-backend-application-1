package com.example.resortbackendapplication1.resortpermissiontype.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.ResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeLocaleDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortPermissionTypeMapper {

    public ResortPermissionTypeEntity create(CreateResortPermissionTypeRequest request) {
        ResortPermissionTypeEntity entity = new ResortPermissionTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortPermissionTypeEntity entity, UpdateResortPermissionTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortPermissionTypeEntity entity, ResortPermissionTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortPermissionTypeDto.ResortPermissionTypeDtoBuilder toDto(ResortPermissionTypeEntity entity) {
        return ResortPermissionTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<ResortPermissionTypeLocaleEntity> activeLocales(ResortPermissionTypeEntity entity) {
        return entity.getResortPermissionTypeLocaleEntities().stream()
                .filter(resortPermissionTypeLocaleEntity -> Boolean.TRUE.equals(resortPermissionTypeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(resortPermissionTypeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private ResortPermissionTypeLocaleDto singleLocale(ResortPermissionTypeEntity entity) {
        ResortPermissionTypeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortPermissionTypeLocaleMapper.toDto(matched);
    }

    private ResortPermissionTypeLocaleEntity matchLocale(ResortPermissionTypeEntity entity, Long localeId) {
        List<ResortPermissionTypeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(resortPermissionTypeLocaleEntity -> resortPermissionTypeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(resortPermissionTypeLocaleEntity -> "en".equals(resortPermissionTypeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
