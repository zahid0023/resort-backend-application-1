package com.example.resortbackendapplication1.resortpermissiontype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.response.resortpermissiontypes.ResortPermissionTypeResponse;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;

public interface ResortPermissionTypeService {

    SuccessResponse create(CreateResortPermissionTypeRequest request,
                           LocaleEntity localeEntity);

    ResortPermissionTypeEntity getEntityById(Long id);

    ResortPermissionTypeResponse getById(Long id);

    PaginatedResponse<ResortPermissionTypeDto> getAll(ResortPermissionTypeFilterRequest request);

    SuccessResponse update(ResortPermissionTypeEntity entity,
                           UpdateResortPermissionTypeRequest request);

    SuccessResponse delete(ResortPermissionTypeEntity entity);
}
