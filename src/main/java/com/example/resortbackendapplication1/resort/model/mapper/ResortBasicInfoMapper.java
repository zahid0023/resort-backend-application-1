package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.ResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortBasicInfoDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortBasicInfoLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortBasicInfoMapper {

    public ResortBasicInfoEntity create(CreateResortBasicInfoRequest request) {
        ResortBasicInfoEntity entity = new ResortBasicInfoEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortBasicInfoEntity entity, UpdateResortBasicInfoRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortBasicInfoEntity entity, ResortBasicInfoRequest request) {
        entity.setEstd(request.getEstd());
        entity.setLogoUrl(request.getLogoUrl());
    }

    public ResortBasicInfoDto.ResortBasicInfoDtoBuilder toDto(ResortBasicInfoEntity entity) {
        return ResortBasicInfoDto.builder()
                .id(entity.getId())
                .estd(entity.getEstd())
                .logoUrl(entity.getLogoUrl())
                .locale(singleLocale(entity));
    }

    private ResortBasicInfoLocaleDto singleLocale(ResortBasicInfoEntity entity) {
        ResortBasicInfoLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortBasicInfoLocaleMapper.toDto(matched);
    }

    private List<ResortBasicInfoLocaleEntity> activeLocales(ResortBasicInfoEntity entity) {
        return entity.getResortBasicInfoLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortBasicInfoLocaleEntity matchLocale(ResortBasicInfoEntity entity, Long localeId) {
        List<ResortBasicInfoLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
