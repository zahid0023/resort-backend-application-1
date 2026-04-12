package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.service.RoomCategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/room-categories")
public class RoomCategoryController {

    private final RoomCategoryService roomCategoryService;

    public RoomCategoryController(RoomCategoryService roomCategoryService) {
        this.roomCategoryService = roomCategoryService;
    }

    @PostMapping
    public ResponseEntity<?> createRoomCategory(@RequestBody CreateRoomCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomCategoryService.createRoomCategory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomCategory(@PathVariable Long id) {
        return ResponseEntity.ok(roomCategoryService.getRoomCategory(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllRoomCategories(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "code", "name"));
        return ResponseEntity.ok(roomCategoryService.getAllRoomCategories(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoomCategory(@PathVariable Long id, @RequestBody UpdateRoomCategoryRequest request) {
        return ResponseEntity.ok(roomCategoryService.updateRoomCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomCategory(@PathVariable Long id) {
        return ResponseEntity.ok(roomCategoryService.deleteRoomCategory(id));
    }
}
