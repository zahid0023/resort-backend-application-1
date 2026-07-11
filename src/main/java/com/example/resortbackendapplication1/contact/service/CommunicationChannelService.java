package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.response.CommunicationChannelResponse;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommunicationChannelService {

    SuccessResponse create(CreateCommunicationChannelRequest request, Map<Long, LocaleEntity> localeEntityMap);

    CommunicationChannelEntity getEntityById(Long id);

    CommunicationChannelResponse getById(Long id);

    PaginatedResponse<CommunicationChannelDto> getAll(CommunicationChannelFilterRequest request);

    SuccessResponse update(CommunicationChannelEntity entity, UpdateCommunicationChannelRequest request);

    SuccessResponse delete(Long id);

    List<CommunicationChannelEntity> getAll(Set<Long> ids);
}
