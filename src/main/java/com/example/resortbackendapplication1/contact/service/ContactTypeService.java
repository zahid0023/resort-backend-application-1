package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.ContactTypeFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.response.contacttypes.ContactTypeResponse;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface ContactTypeService {

    SuccessResponse create(CreateContactTypeRequest request,
                           LocaleEntity localeEntity);

    ContactTypeEntity getEntityById(Long id);

    ContactTypeResponse getById(Long id);

    PaginatedResponse<ContactTypeDto> getAll(ContactTypeFilterRequest request);

    SuccessResponse update(ContactTypeEntity entity,
                           UpdateContactTypeRequest request);

    SuccessResponse delete(ContactTypeEntity entity);
}
