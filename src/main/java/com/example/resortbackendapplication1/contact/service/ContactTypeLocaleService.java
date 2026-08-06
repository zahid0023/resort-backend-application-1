package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface ContactTypeLocaleService {
    SuccessResponse create(CreateContactTypeLocaleRequest request,
                           ContactTypeEntity contactTypeEntity,
                           LocaleEntity localeEntity);

    ContactTypeLocaleEntity getEntityById(Long contactTypeId, Long id);

    PaginatedResponse<ContactTypeLocaleDto> getAll(Long contactTypeId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long contactTypeId);

    SuccessResponse update(ContactTypeLocaleEntity entity,
                           UpdateContactTypeLocaleRequest request);

    SuccessResponse delete(ContactTypeLocaleEntity entity);
}
