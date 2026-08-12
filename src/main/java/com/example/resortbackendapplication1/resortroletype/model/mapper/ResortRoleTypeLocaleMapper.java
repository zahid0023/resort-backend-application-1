package com.example.resortbackendapplication1.resortroletype.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.ResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.UpdateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeLocaleDto;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoleTypeLocaleMapper {

    public ResortRoleTypeLocaleEntity create(ResortRoleTypeLocaleRequest request) {
        ResortRoleTypeLocaleEntity entity = new ResortRoleTypeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoleTypeLocaleEntity entity, UpdateResortRoleTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoleTypeLocaleEntity entity, ResortRoleTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortRoleTypeLocaleDto toDto(ResortRoleTypeLocaleEntity entity) {
        return ResortRoleTypeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
