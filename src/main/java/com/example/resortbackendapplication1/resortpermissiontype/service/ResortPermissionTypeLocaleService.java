package com.example.resortbackendapplication1.resortpermissiontype.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeLocaleDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;

public interface ResortPermissionTypeLocaleService {
    SuccessResponse create(CreateResortPermissionTypeLocaleRequest request,
                           ResortPermissionTypeEntity resortPermissionTypeEntity,
                           LocaleEntity localeEntity);

    ResortPermissionTypeLocaleEntity getEntityById(Long resortPermissionTypeId, Long id);

    PaginatedResponse<ResortPermissionTypeLocaleDto> getAll(Long resortPermissionTypeId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(ResortPermissionTypeLocaleEntity entity,
                           UpdateResortPermissionTypeLocaleRequest request);

    SuccessResponse delete(ResortPermissionTypeLocaleEntity entity);
}
