package com.example.resortbackendapplication1.resortpermissiontype.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;

public interface ResortPermissionTypeLocaleService {

    SuccessResponse create(ResortPermissionTypeEntity resortPermissionTypeEntity,
                           LocaleEntity localeEntity,
                           CreateResortPermissionTypeLocaleRequest request);

    ResortPermissionTypeLocaleEntity getEntityById(Long resortPermissionTypeId, Long id);

    SuccessResponse update(ResortPermissionTypeLocaleEntity entity, UpdateResortPermissionTypeLocaleRequest request);

    SuccessResponse delete(ResortPermissionTypeLocaleEntity entity);
}
