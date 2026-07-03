package com.example.resortbackendapplication1.roomcategory.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryLocaleService;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room-categories/{room-category-id}/locales")
public class RoomCategoryLocaleController {

    private final RoomCategoryService roomCategoryService;
    private final RoomCategoryLocaleService roomCategoryLocaleService;
    private final LocaleService localeService;

    public RoomCategoryLocaleController(RoomCategoryService roomCategoryService,
                                        RoomCategoryLocaleService roomCategoryLocaleService,
                                        LocaleService localeService) {
        this.roomCategoryService = roomCategoryService;
        this.roomCategoryLocaleService = roomCategoryLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable("room-category-id") Long roomCategoryId,
                                    @Valid @RequestBody CreateRoomCategoryLocaleRequest request) {
        RoomCategoryEntity roomCategory = roomCategoryService.getEntityById(roomCategoryId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomCategoryLocaleService.create(roomCategory, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("room-category-id") Long roomCategoryId,
                                    @PathVariable Long id,
                                    @Valid @RequestBody UpdateRoomCategoryLocaleRequest request) {
        RoomCategoryLocaleEntity entity = roomCategoryLocaleService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(roomCategoryLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("room-category-id") Long roomCategoryId,
                                    @PathVariable Long id) {
        RoomCategoryLocaleEntity entity = roomCategoryLocaleService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(roomCategoryLocaleService.delete(entity));
    }
}
