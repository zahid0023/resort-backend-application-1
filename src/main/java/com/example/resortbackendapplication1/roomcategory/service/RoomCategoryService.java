package com.example.resortbackendapplication1.roomcategory.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryFilterRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.response.roomcategories.RoomCategoryResponse;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoomCategoryService {

    SuccessResponse create(CreateRoomCategoryRequest request, Map<Long, LocaleEntity> localeEntityMap);

    RoomCategoryEntity getEntityById(Long id);

    RoomCategoryResponse getById(Long id);

    PaginatedResponse<RoomCategoryDto> getAll(RoomCategoryFilterRequest request);

    SuccessResponse update(RoomCategoryEntity entity, UpdateRoomCategoryRequest request);

    SuccessResponse delete(Long id);

    List<RoomCategoryEntity> getAll(Set<Long> ids);
}
