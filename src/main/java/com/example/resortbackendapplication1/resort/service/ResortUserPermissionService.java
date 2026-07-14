package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.CreateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.UpdateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortUserPermissionResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortUserPermissionDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;

import java.util.Map;

public interface ResortUserPermissionService {

    SuccessResponse create(CreateResortUserPermissionRequest request,
                           ResortUserEntity resortUserEntity,
                           Map<Long, ResortPermissionTypeEntity> permissionTypeEntityMap);

    ResortUserPermissionEntity getEntityById(Long id, Long resortUserId);

    ResortUserPermissionResponse getById(Long id, Long resortUserId);

    PaginatedResponse<ResortUserPermissionDto> getAll(Long resortUserId, PaginatedRequest request);

    SuccessResponse update(ResortUserPermissionEntity entity, UpdateResortUserPermissionRequest request);

    SuccessResponse delete(ResortUserPermissionEntity entity);
}
