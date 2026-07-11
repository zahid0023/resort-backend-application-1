package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.CommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CommunicationChannelMapper {

    public CommunicationChannelEntity create(CreateCommunicationChannelRequest request,
                                             Map<Long, LocaleEntity> localeEntityMap) {
        CommunicationChannelEntity entity = new CommunicationChannelEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setCommunicationChannelLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(CommunicationChannelEntity entity, UpdateCommunicationChannelRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CommunicationChannelEntity entity, CommunicationChannelRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIsUrl(request.getIsUrl());
        entity.setIsPhone(request.getIsPhone());
        entity.setIsEmail(request.getIsEmail());
        entity.setIsClickable(request.getIsClickable());
    }

    private Set<CommunicationChannelLocaleEntity> mapLocales(List<CreateCommunicationChannelLocaleRequest> locales,
                                                              CommunicationChannelEntity entity,
                                                              Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> CommunicationChannelLocaleMapper.create(
                        locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public CommunicationChannelDto toDto(CommunicationChannelEntity entity) {
        List<CommunicationChannelLocaleDto> locales = entity.getCommunicationChannelLocaleEntities().stream()
                .map(CommunicationChannelLocaleMapper::toDto)
                .toList();
        return CommunicationChannelDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .isUrl(entity.getIsUrl())
                .isPhone(entity.getIsPhone())
                .isEmail(entity.getIsEmail())
                .isClickable(entity.getIsClickable())
                .locales(locales)
                .build();
    }
}
