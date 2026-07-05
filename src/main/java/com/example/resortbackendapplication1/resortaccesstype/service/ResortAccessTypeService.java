package com.example.resortbackendapplication1.resortaccesstype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.CreateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.ResortAccessTypeFilterRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.UpdateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.response.ResortAccessTypeResponse;
import com.example.resortbackendapplication1.resortaccesstype.model.dto.ResortAccessTypeDto;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResortAccessTypeService {

    SuccessResponse create(CreateResortAccessTypeRequest request, Map<Long, LocaleEntity> localeEntityMap);

    ResortAccessTypeEntity getEntityById(Long id);

    ResortAccessTypeResponse getById(Long id);

    PaginatedResponse<ResortAccessTypeDto> getAll(ResortAccessTypeFilterRequest request);

    SuccessResponse update(ResortAccessTypeEntity entity, UpdateResortAccessTypeRequest request);

    SuccessResponse delete(Long id);

    List<ResortAccessTypeEntity> getAll(Set<Long> ids);
}
