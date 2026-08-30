package com.example.resortbackendapplication1.resort.availability.service;

import com.example.resortbackendapplication1.resort.availability.dto.request.availability.AvailabilityRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;

import java.util.List;

public interface AvailabilityService {

    /**
     * Every active room in the resort that's both room_status AVAILABLE and free of an overlapping active
     * reservation for [checkIn, checkOut) — the same shared logic both POS and (later) the website booking
     * flow use, so availability can never drift between channels. Returns raw entities, not DTOs — building
     * the full ResortRoomDto (own-vs-inherited meta/beds) needs ResortRoomCategoryMetaService/
     * ResortRoomCategoryBedService, which this Service must never call directly; AvailabilityController does
     * that assembly via ResortRoomService#buildDto.
     */
    List<ResortRoomEntity> search(Long resortId, AvailabilityRequest request);
}
