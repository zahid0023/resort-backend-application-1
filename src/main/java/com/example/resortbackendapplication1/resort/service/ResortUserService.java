package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.response.ResortUserResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortUserDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;

public interface ResortUserService {

    boolean isOwner(Long resortId, Long userId);

    SuccessResponse create(ResortEntity resortEntity, UserEntity userEntity, ResortAccessTypeEntity resortAccessTypeEntity);

    ResortUserEntity getEntityById(Long id, Long resortId);

    ResortUserResponse getById(Long id, Long resortId);

    PaginatedResponse<ResortUserDto> getAll(Long resortId, PaginatedRequest request);

    SuccessResponse update(ResortUserEntity entity, ResortAccessTypeEntity resortAccessTypeEntity);

    SuccessResponse delete(ResortUserEntity entity);
}
