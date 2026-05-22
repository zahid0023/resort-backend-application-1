package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.locale.CreateLocaleRequest;
import com.example.resortbackendapplication1.dto.request.locale.UpdateLocaleRequest;
import com.example.resortbackendapplication1.dto.response.locales.LocaleResponse;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.projection.LocaleSummary;

import java.util.List;
import java.util.Set;

public interface LocaleService {
    SuccessResponse create(CreateLocaleRequest request);

    LocaleEntity getEntityById(Long id);

    LocaleResponse getById(Long id);

    PaginatedResponse<LocaleSummary> getAll(PaginatedRequest request);

    SuccessResponse update(LocaleEntity entity, UpdateLocaleRequest request);

    SuccessResponse delete(Long id);

    List<LocaleEntity> getAll(Set<Long> ids);
}
