package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.locale.ResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.locale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortBasicInfoLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortBasicInfoLocaleMapper {

    public ResortBasicInfoLocaleEntity create(ResortBasicInfoLocaleRequest request) {
        ResortBasicInfoLocaleEntity entity = new ResortBasicInfoLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortBasicInfoLocaleEntity entity, UpdateResortBasicInfoLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortBasicInfoLocaleEntity entity, ResortBasicInfoLocaleRequest request) {
        entity.setName(request.getName());
        entity.setTagline(request.getTagline());
        entity.setShortDescription(request.getShortDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortBasicInfoLocaleDto toDto(ResortBasicInfoLocaleEntity entity) {
        return ResortBasicInfoLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .sortOrder(entity.getSortOrder())
                .name(entity.getName())
                .tagline(entity.getTagline())
                .shortDescription(entity.getShortDescription())
                .build();
    }
}
