package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CommunicationChannelMapper {

    public CommunicationChannelEntity create(CreateCommunicationChannelRequest request) {
        CommunicationChannelEntity entity = new CommunicationChannelEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
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

    public CommunicationChannelDto.CommunicationChannelDtoBuilder toDto(CommunicationChannelEntity entity) {
        return CommunicationChannelDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .isUrl(entity.getIsUrl())
                .isPhone(entity.getIsPhone())
                .isEmail(entity.getIsEmail())
                .isClickable(entity.getIsClickable())
                .locale(singleLocale(entity));
    }

    private List<CommunicationChannelLocaleEntity> activeLocales(CommunicationChannelEntity entity) {
        return entity.getCommunicationChannelLocaleEntities().stream()
                .filter(communicationChannelLocaleEntity -> Boolean.TRUE.equals(communicationChannelLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(communicationChannelLocaleEntity.getIsDeleted()))
                .toList();
    }

    private CommunicationChannelLocaleDto singleLocale(CommunicationChannelEntity entity) {
        CommunicationChannelLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : CommunicationChannelLocaleMapper.toDto(matched);
    }

    private CommunicationChannelLocaleEntity matchLocale(CommunicationChannelEntity entity, Long localeId) {
        List<CommunicationChannelLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(communicationChannelLocaleEntity -> communicationChannelLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(communicationChannelLocaleEntity -> "en".equals(communicationChannelLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
