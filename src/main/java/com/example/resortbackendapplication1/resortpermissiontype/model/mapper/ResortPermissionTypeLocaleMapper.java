package com.example.resortbackendapplication1.resortpermissiontype.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.ResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeLocaleDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortPermissionTypeLocaleMapper {

    public ResortPermissionTypeLocaleEntity create(ResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeLocaleEntity entity = new ResortPermissionTypeLocaleEntity();
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
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
