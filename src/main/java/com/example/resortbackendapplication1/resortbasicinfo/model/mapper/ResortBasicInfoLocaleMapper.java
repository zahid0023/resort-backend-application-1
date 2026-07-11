package com.example.resortbackendapplication1.resortbasicinfo.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.ResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoLocaleDto;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortBasicInfoLocaleMapper {

    public ResortBasicInfoLocaleEntity create(CreateResortBasicInfoLocaleRequest request,
                                              ResortBasicInfoEntity resortBasicInfoEntity,
                                              LocaleEntity localeEntity) {
        ResortBasicInfoLocaleEntity entity = new ResortBasicInfoLocaleEntity();
        entity.setResortBasicInfoEntity(resortBasicInfoEntity);
        entity.setLocaleEntity(localeEntity);
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
        entity.setAddress(request.getAddress());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortBasicInfoLocaleDto toDto(ResortBasicInfoLocaleEntity entity) {
        return ResortBasicInfoLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .sortOrder(entity.getSortOrder())
                .name(entity.getName())
                .tagline(entity.getTagline())
                .shortDescription(entity.getShortDescription())
                .address(entity.getAddress())
                .build();
    }
}
