package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.service.LocaleService;
import com.example.resortbackendapplication1.service.RoomCategoryService;
import com.example.resortbackendapplication1.utils.LocaleUtils;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/room-categories")
public class RoomCategoryController {

    private final RoomCategoryService roomCategoryService;
    private final LocaleService localeService;

    public RoomCategoryController(RoomCategoryService roomCategoryService,
                                  LocaleService localeService) {
        this.roomCategoryService = roomCategoryService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRoomCategoryRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateRoomCategoryLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomCategoryService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomCategoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(roomCategoryService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomCategoryRequest request) {
        RoomCategoryEntity entity = roomCategoryService.getEntityById(id);
        return ResponseEntity.ok(roomCategoryService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(roomCategoryService.delete(id));
    }
}
