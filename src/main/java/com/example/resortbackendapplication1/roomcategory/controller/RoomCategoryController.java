package com.example.resortbackendapplication1.roomcategory.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryFilterRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryService;
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
    public ResponseEntity<?> getAll(@Valid @ParameterObject RoomCategoryFilterRequest request) {
        return ResponseEntity.ok(roomCategoryService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody UpdateRoomCategoryRequest request) {
        RoomCategoryEntity entity = roomCategoryService.getEntityById(id);
        return ResponseEntity.ok(roomCategoryService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(roomCategoryService.delete(id));
    }
}
