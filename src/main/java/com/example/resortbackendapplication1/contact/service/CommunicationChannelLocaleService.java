package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface CommunicationChannelLocaleService {
    SuccessResponse create(CreateCommunicationChannelLocaleRequest request,
                           CommunicationChannelEntity communicationChannelEntity,
                           LocaleEntity localeEntity);

    CommunicationChannelLocaleEntity getEntityById(Long communicationChannelId, Long id);

    PaginatedResponse<CommunicationChannelLocaleDto> getAll(Long communicationChannelId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(CommunicationChannelLocaleEntity entity,
                           UpdateCommunicationChannelLocaleRequest request);

    SuccessResponse delete(CommunicationChannelLocaleEntity entity);
}
