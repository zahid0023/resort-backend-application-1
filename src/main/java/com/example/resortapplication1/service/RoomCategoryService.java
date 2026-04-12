package com.example.resortapplication1.service;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.roomcategories.CreateRoomCategoryRequest;
import com.example.resortapplication1.dto.request.roomcategories.UpdateRoomCategoryRequest;
import com.example.resortapplication1.dto.response.roomcategories.RoomCategoryResponse;
import com.example.resortapplication1.model.dto.RoomCategoryDto;
import com.example.resortapplication1.model.entity.RoomCategoryEntity;
import org.springframework.data.domain.Pageable;

public interface RoomCategoryService {

    SuccessResponse createRoomCategory(CreateRoomCategoryRequest request);

    RoomCategoryEntity getRoomCategoryEntity(Long id);

    RoomCategoryResponse getRoomCategory(Long id);

    PaginatedResponse<RoomCategoryDto> getAllRoomCategories(Pageable pageable);

    SuccessResponse updateRoomCategory(Long id, UpdateRoomCategoryRequest request);

    SuccessResponse deleteRoomCategory(Long id);
}
