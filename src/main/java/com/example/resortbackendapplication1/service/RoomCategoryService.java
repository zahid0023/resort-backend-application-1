package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.roomcategories.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.response.roomcategories.RoomCategoryResponse;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.projection.RoomCategorySummary;

import java.util.Map;

public interface RoomCategoryService {

    SuccessResponse create(CreateRoomCategoryRequest request, Map<Long, LocaleEntity> localeEntityMap);

    RoomCategoryEntity getEntityById(Long id);

    RoomCategoryResponse getById(Long id);

    PaginatedResponse<RoomCategorySummary> getAll(PaginatedRequest request);

    SuccessResponse update(RoomCategoryEntity entity, UpdateRoomCategoryRequest request);

    SuccessResponse delete(Long id);
}
