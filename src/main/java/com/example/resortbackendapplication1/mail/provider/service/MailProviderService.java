package com.example.resortbackendapplication1.mail.provider.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.CreateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.MailProviderFilterRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.UpdateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.response.mailproviders.MailProviderResponse;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;

public interface MailProviderService {

    SuccessResponse create(CreateMailProviderRequest request);

    MailProviderEntity getEntityById(Long id);

    MailProviderResponse getById(Long id);

    PaginatedResponse<MailProviderDto> getAll(MailProviderFilterRequest request);

    SuccessResponse update(MailProviderEntity entity, UpdateMailProviderRequest request);

    SuccessResponse delete(MailProviderEntity entity);
}
