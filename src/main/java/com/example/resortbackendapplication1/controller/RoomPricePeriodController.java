package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.CreateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.UpdateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import com.example.resortbackendapplication1.service.RoomPricePeriodService;
import com.example.resortbackendapplication1.service.RoomService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods")
public class RoomPricePeriodController {

    private final RoomPricePeriodService roomPricePeriodService;
    private final RoomService roomService;

    public RoomPricePeriodController(RoomPricePeriodService roomPricePeriodService, RoomService roomService) {
        this.roomPricePeriodService = roomPricePeriodService;
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<?> createRoomPricePeriod(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @RequestBody CreateRoomPricePeriodRequest request) {
        RoomEntity roomEntity = roomService.getRoomEntity(resortRoomCategoryId, roomId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomPricePeriodService.createRoomPricePeriod(request, roomEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomPricePeriod(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long ignoredResortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @PathVariable Long id) {
        return ResponseEntity.ok(roomPricePeriodService.getRoomPricePeriod(roomId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllRoomPricePeriods(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long ignoredResortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "startDate", "endDate", "price", "priority"));
        return ResponseEntity.ok(roomPricePeriodService.getAllRoomPricePeriods(roomId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoomPricePeriod(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long ignoredResortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @PathVariable Long id,
            @RequestBody UpdateRoomPricePeriodRequest request) {
        return ResponseEntity.ok(roomPricePeriodService.updateRoomPricePeriod(roomId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomPricePeriod(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long ignoredResortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @PathVariable Long id) {
        return ResponseEntity.ok(roomPricePeriodService.deleteRoomPricePeriod(roomId, id));
    }
}
