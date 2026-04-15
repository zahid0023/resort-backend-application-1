package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.rooms.CreateRoomRequest;
import com.example.resortbackendapplication1.dto.request.rooms.UpdateRoomRequest;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import com.example.resortbackendapplication1.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.service.RoomService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms")
public class RoomController {

    private final RoomService roomService;
    private final ResortRoomCategoryService resortRoomCategoryService;

    public RoomController(RoomService roomService, ResortRoomCategoryService resortRoomCategoryService) {
        this.roomService = roomService;
        this.resortRoomCategoryService = resortRoomCategoryService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @RequestBody CreateRoomRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getResortRoomCategoryEntity(ignoredResortId, resortRoomCategoryId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.createRoom(request, resortRoomCategoryEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoom(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoom(resortRoomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "name", "roomNumber", "floor", "basePrice"));
        return ResponseEntity.ok(roomService.getAllRooms(resortRoomCategoryId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @RequestBody UpdateRoomRequest request) {
        RoomEntity entity = roomService.getRoomEntity(resortRoomCategoryId, id);
        return ResponseEntity.ok(roomService.updateRoom(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        return ResponseEntity.ok(roomService.deleteRoom(resortRoomCategoryId, id));
    }
}
