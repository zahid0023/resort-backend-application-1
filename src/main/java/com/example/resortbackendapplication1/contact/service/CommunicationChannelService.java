package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.response.communicationchannels.CommunicationChannelResponse;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface CommunicationChannelService {

    SuccessResponse create(CreateCommunicationChannelRequest request,
                           LocaleEntity localeEntity);

    CommunicationChannelEntity getEntityById(Long id);

    CommunicationChannelResponse getById(Long id);

    PaginatedResponse<CommunicationChannelDto> getAll(CommunicationChannelFilterRequest request);

    SuccessResponse update(CommunicationChannelEntity entity,
                           UpdateCommunicationChannelRequest request);

    SuccessResponse delete(CommunicationChannelEntity entity);
}
