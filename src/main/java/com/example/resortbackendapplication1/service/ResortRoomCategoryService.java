package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.response.resortroomcategories.ResortRoomCategoryResponse;
import com.example.resortbackendapplication1.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import org.springframework.data.domain.Pageable;

public interface ResortRoomCategoryService {

    SuccessResponse createResortRoomCategory(CreateResortRoomCategoryRequest request,
                                             ResortEntity resortEntity,
                                             RoomCategoryEntity roomCategoryEntity);

    ResortRoomCategoryEntity getResortRoomCategoryEntity(Long resortId, Long id);

    ResortRoomCategoryResponse getResortRoomCategory(Long resortId, Long id);

    PaginatedResponse<ResortRoomCategoryDto> getAllResortRoomCategories(Long resortId, Pageable pageable);

    SuccessResponse updateResortRoomCategory(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request);

    SuccessResponse deleteResortRoomCategory(Long resortId, Long id);
}
