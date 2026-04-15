package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.service.ResortService;
import com.example.resortbackendapplication1.service.RoomCategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-room-categories")
public class ResortRoomCategoryController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortService resortService;
    private final RoomCategoryService roomCategoryService;

    public ResortRoomCategoryController(ResortRoomCategoryService resortRoomCategoryService,
                                        ResortService resortService,
                                        RoomCategoryService roomCategoryService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortService = resortService;
        this.roomCategoryService = roomCategoryService;
    }

    @PostMapping
    public ResponseEntity<?> createResortRoomCategory(
            @PathVariable("resort-id") Long resortId,
            @RequestBody CreateResortRoomCategoryRequest request) {
        ResortEntity resort = resortService.getResortById(resortId);
        RoomCategoryEntity roomCategory = roomCategoryService.getRoomCategoryEntity(request.getRoomCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryService.createResortRoomCategory(request, resort, roomCategory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResortRoomCategory(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryService.getResortRoomCategory(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllResortRoomCategories(
            @PathVariable("resort-id") Long resortId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "name", "sortOrder"));
        return ResponseEntity.ok(resortRoomCategoryService.getAllResortRoomCategories(resortId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortRoomCategory(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @RequestBody UpdateResortRoomCategoryRequest request) {
        ResortRoomCategoryEntity entity = resortRoomCategoryService.getResortRoomCategoryEntity(resortId, id);
        return ResponseEntity.ok(resortRoomCategoryService.updateResortRoomCategory(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortRoomCategory(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryService.deleteResortRoomCategory(resortId, id));
    }
}
