package com.example.resortbackendapplication1.roomstatus.service;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.RoomStatusFilterRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.CreateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.UpdateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.response.roomstatuses.RoomStatusResponse;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusDto;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface RoomStatusService {

    SuccessResponse create(CreateRoomStatusRequest request,
                           LocaleEntity localeEntity);

    RoomStatusEntity getEntityById(Long id);

    RoomStatusResponse getById(Long id);

    PaginatedResponse<RoomStatusDto> getAll(RoomStatusFilterRequest request);

    SuccessResponse update(RoomStatusEntity entity,
                           UpdateRoomStatusRequest request);

    SuccessResponse delete(RoomStatusEntity entity);
}
