package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.locale.CommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommunicationChannelLocaleMapper {

    public CommunicationChannelLocaleEntity create(CreateCommunicationChannelLocaleRequest request,
                                                   CommunicationChannelEntity channelEntity,
                                                   LocaleEntity localeEntity) {
        CommunicationChannelLocaleEntity entity = new CommunicationChannelLocaleEntity();
        entity.setCommunicationChannelEntity(channelEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(CommunicationChannelLocaleEntity entity, UpdateCommunicationChannelLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CommunicationChannelLocaleEntity entity, CommunicationChannelLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public CommunicationChannelLocaleDto toDto(CommunicationChannelLocaleEntity entity) {
        return CommunicationChannelLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
