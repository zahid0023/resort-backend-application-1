package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.userphone.CreateUserPhoneRequest;
import com.example.resortbackendapplication1.contact.dto.request.userphone.UpdateUserPhoneRequest;
import com.example.resortbackendapplication1.contact.dto.response.userphones.UserPhoneResponse;
import com.example.resortbackendapplication1.contact.model.dto.UserPhoneDto;
import com.example.resortbackendapplication1.contact.model.entity.UserPhoneEntity;

public interface UserPhoneService {

    SuccessResponse create(CreateUserPhoneRequest request, UserEntity userEntity);

    UserPhoneEntity getEntityById(Long userId, Long id);

    UserPhoneResponse getById(Long userId, Long id);

    PaginatedResponse<UserPhoneDto> getAll(Long userId, PaginatedRequest paginatedRequest);

    SuccessResponse update(UserPhoneEntity entity, UpdateUserPhoneRequest request);

    SuccessResponse delete(UserPhoneEntity entity);
}
