package com.example.resortbackendapplication1.resortroomcategory.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.response.ResortRoomCategoryResponse;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;

import java.util.Map;

public interface ResortRoomCategoryService {

    SuccessResponse create(CreateResortRoomCategoryRequest request,
                           ResortEntity resortEntity,
                           RoomCategoryEntity roomCategoryEntity,
                           Map<Long, LocaleEntity> localeEntityMap);

    ResortRoomCategoryEntity getEntityById(Long resortId, Long id);

    ResortRoomCategoryResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortRoomCategoryDto> getAll(ResortRoomCategoryFilterRequest request, Long resortId);

    SuccessResponse update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request);

    SuccessResponse delete(ResortRoomCategoryEntity entity);
}
