package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.ContactTypeFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.response.ContactTypeResponse;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContactTypeService {

    SuccessResponse create(CreateContactTypeRequest request, Map<Long, LocaleEntity> localeEntityMap);

    ContactTypeEntity getEntityById(Long id);

    ContactTypeResponse getById(Long id);

    PaginatedResponse<ContactTypeDto> getAll(ContactTypeFilterRequest request);

    SuccessResponse update(ContactTypeEntity entity, UpdateContactTypeRequest request);

    SuccessResponse delete(Long id);

    List<ContactTypeEntity> getAll(Set<Long> ids);
}
