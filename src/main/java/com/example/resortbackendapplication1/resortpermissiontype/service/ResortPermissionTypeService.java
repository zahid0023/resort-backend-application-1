package com.example.resortbackendapplication1.resortpermissiontype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.response.ResortPermissionTypeResponse;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResortPermissionTypeService {

    SuccessResponse create(CreateResortPermissionTypeRequest request, Map<Long, LocaleEntity> localeEntityMap);

    ResortPermissionTypeEntity getEntityById(Long id);

    ResortPermissionTypeResponse getById(Long id);

    PaginatedResponse<ResortPermissionTypeDto> getAll(ResortPermissionTypeFilterRequest request);

    SuccessResponse update(ResortPermissionTypeEntity entity, UpdateResortPermissionTypeRequest request);

    SuccessResponse delete(Long id);

    List<ResortPermissionTypeEntity> getAll(Set<Long> ids);
}
