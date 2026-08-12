package com.example.resortbackendapplication1.resortroletype.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.CreateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.ResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.UpdateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeDto;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeLocaleDto;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortRoleTypeMapper {

    public ResortRoleTypeEntity create(CreateResortRoleTypeRequest request) {
        ResortRoleTypeEntity entity = new ResortRoleTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoleTypeEntity entity, UpdateResortRoleTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoleTypeEntity entity, ResortRoleTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoleTypeDto.ResortRoleTypeDtoBuilder toDto(ResortRoleTypeEntity entity) {
        return ResortRoleTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<ResortRoleTypeLocaleEntity> activeLocales(ResortRoleTypeEntity entity) {
        return entity.getResortRoleTypeLocaleEntities().stream()
                .filter(resortRoleTypeLocaleEntity -> Boolean.TRUE.equals(resortRoleTypeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(resortRoleTypeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private ResortRoleTypeLocaleDto singleLocale(ResortRoleTypeEntity entity) {
        ResortRoleTypeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortRoleTypeLocaleMapper.toDto(matched);
    }

    private ResortRoleTypeLocaleEntity matchLocale(ResortRoleTypeEntity entity, Long localeId) {
        List<ResortRoleTypeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(resortRoleTypeLocaleEntity -> resortRoleTypeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(resortRoleTypeLocaleEntity -> "en".equals(resortRoleTypeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
