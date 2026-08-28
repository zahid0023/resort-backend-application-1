package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.CreateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.UpdateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomLocaleService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales")
public class ResortRoomLocaleController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final ResortRoomLocaleService resortRoomLocaleService;
    private final LocaleService localeService;

    public ResortRoomLocaleController(ResortRoomCategoryService resortRoomCategoryService,
                                      ResortRoomService resortRoomService,
                                      ResortRoomLocaleService resortRoomLocaleService,
                                      LocaleService localeService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.resortRoomLocaleService = resortRoomLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        return ResponseEntity.ok(resortRoomLocaleService.getAll(resortRoomEntity.getId(), localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        return ResponseEntity.ok(resortRoomLocaleService.getActiveCount(resortRoomEntity.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomLocaleRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomLocaleService.create(request, resortRoomEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomLocaleRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        ResortRoomLocaleEntity entity = resortRoomLocaleService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        ResortRoomLocaleEntity entity = resortRoomLocaleService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomLocaleService.delete(entity));
    }
}
