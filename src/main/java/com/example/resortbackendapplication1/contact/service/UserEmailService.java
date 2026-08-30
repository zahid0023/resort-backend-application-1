package com.example.resortbackendapplication1.contact.service;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.useremail.CreateUserEmailRequest;
import com.example.resortbackendapplication1.contact.dto.request.useremail.UpdateUserEmailRequest;
import com.example.resortbackendapplication1.contact.dto.response.useremails.UserEmailResponse;
import com.example.resortbackendapplication1.contact.model.dto.UserEmailDto;
import com.example.resortbackendapplication1.contact.model.entity.UserEmailEntity;

public interface UserEmailService {

    SuccessResponse create(CreateUserEmailRequest request, UserEntity userEntity);

    UserEmailEntity getEntityById(Long userId, Long id);

    UserEmailResponse getById(Long userId, Long id);

    PaginatedResponse<UserEmailDto> getAll(Long userId, PaginatedRequest paginatedRequest);

    SuccessResponse update(UserEmailEntity entity, UpdateUserEmailRequest request);

    SuccessResponse delete(UserEmailEntity entity);
}
