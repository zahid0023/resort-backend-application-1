package com.example.resortbackendapplication1.mail.provider.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.CreateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.MailProviderConfigFilterRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.UpdateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderConfigDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;

import java.util.Optional;

public interface MailProviderConfigService {

    SuccessResponse create(CreateMailProviderConfigRequest request,
                           MailProviderEntity providerEntity);

    MailProviderConfigEntity getEntityById(Long mailProviderId, Long id);

    MailProviderConfigEntity getEntityById(Long id);

    /**
     * Not every purpose code is guaranteed to have a config assigned to it yet — callers (e.g. the POS booking
     * flow) should treat an empty result as "not configured" and degrade gracefully rather than failing.
     */
    Optional<MailProviderConfigEntity> getEntityByCode(MailProviderConfigCode code);

    PaginatedResponse<MailProviderConfigDto> getAll(MailProviderConfigFilterRequest request);

    SuccessResponse update(MailProviderConfigEntity entity, UpdateMailProviderConfigRequest request);

    SuccessResponse delete(MailProviderConfigEntity entity);
}
