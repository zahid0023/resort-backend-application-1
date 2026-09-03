package com.example.resortbackendapplication1.mail.provider.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.CreateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.UpdateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderConfigFieldDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigFieldEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;

import java.util.List;

public interface MailProviderConfigFieldService {

    SuccessResponse create(CreateMailProviderConfigFieldRequest request,
                           MailProviderEntity providerEntity);

    MailProviderConfigFieldEntity getEntityById(Long mailProviderId, Long id);

    List<MailProviderConfigFieldDto> getAll(Long mailProviderId);

    SuccessResponse update(MailProviderConfigFieldEntity entity, UpdateMailProviderConfigFieldRequest request);

    SuccessResponse delete(MailProviderConfigFieldEntity entity);
}
