package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.CreateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.UpdateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.dto.response.roompriceperiods.RoomPricePeriodResponse;
import com.example.resortbackendapplication1.model.dto.RoomPricePeriodDto;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import org.springframework.data.domain.Pageable;

public interface RoomPricePeriodService {
    SuccessResponse createRoomPricePeriod(CreateRoomPricePeriodRequest request, RoomEntity roomEntity);
    RoomPricePeriodResponse getRoomPricePeriod(Long roomId, Long id);
    PaginatedResponse<RoomPricePeriodDto> getAllRoomPricePeriods(Long roomId, Pageable pageable);
    SuccessResponse updateRoomPricePeriod(Long roomId, Long id, UpdateRoomPricePeriodRequest request);
    SuccessResponse deleteRoomPricePeriod(Long roomId, Long id);
}
