package com.example.resortbackendapplication1.pagetype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.CreatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.PageTypeFilterRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.UpdatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.response.pagetypes.PageTypeResponse;
import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeDto;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PageTypeService {

    SuccessResponse create(CreatePageTypeRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    PageTypeEntity getEntityById(Long id);

    PageTypeResponse getById(Long id);

    PaginatedResponse<PageTypeDto> getAll(PageTypeFilterRequest request);

    SuccessResponse update(PageTypeEntity entity,
                           UpdatePageTypeRequest request);

    SuccessResponse delete(Long id);

    List<PageTypeEntity> getAll(Set<Long> ids);
}
