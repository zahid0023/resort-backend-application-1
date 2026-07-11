package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface CommunicationChannelLocaleService {

    SuccessResponse create(CommunicationChannelEntity channelEntity,
                           LocaleEntity localeEntity,
                           CreateCommunicationChannelLocaleRequest request);

    CommunicationChannelLocaleEntity getEntityById(Long channelId, Long id);

    SuccessResponse update(CommunicationChannelLocaleEntity entity, UpdateCommunicationChannelLocaleRequest request);

    SuccessResponse delete(CommunicationChannelLocaleEntity entity);
}
