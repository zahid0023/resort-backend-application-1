package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.rooms.CreateRoomRequest;
import com.example.resortbackendapplication1.dto.request.rooms.UpdateRoomRequest;
import com.example.resortbackendapplication1.dto.response.rooms.RoomResponse;
import com.example.resortbackendapplication1.model.dto.RoomDto;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import org.springframework.data.domain.Pageable;

public interface RoomService {

    SuccessResponse createRoom(CreateRoomRequest request, ResortRoomCategoryEntity resortRoomCategoryEntity);

    RoomEntity getRoomEntity(Long resortRoomCategoryId, Long id);

    RoomResponse getRoom(Long resortRoomCategoryId, Long id);

    PaginatedResponse<RoomDto> getAllRooms(Long resortRoomCategoryId, Pageable pageable);

    SuccessResponse updateRoom(RoomEntity entity, UpdateRoomRequest request);

    SuccessResponse deleteRoom(Long resortRoomCategoryId, Long id);
}
