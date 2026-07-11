package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface ContactTypeLocaleService {

    SuccessResponse create(ContactTypeEntity contactTypeEntity,
                           LocaleEntity localeEntity,
                           CreateContactTypeLocaleRequest request);

    ContactTypeLocaleEntity getEntityById(Long contactTypeId, Long id);

    SuccessResponse update(ContactTypeLocaleEntity entity, UpdateContactTypeLocaleRequest request);

    SuccessResponse delete(ContactTypeLocaleEntity entity);
}
