package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales")
public class ResortRoomCategoryLocaleController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomCategoryLocaleService resortRoomCategoryLocaleService;
    private final LocaleService localeService;

    public ResortRoomCategoryLocaleController(ResortRoomCategoryService resortRoomCategoryService,
                                              ResortRoomCategoryLocaleService resortRoomCategoryLocaleService,
                                              LocaleService localeService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomCategoryLocaleService = resortRoomCategoryLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryLocaleService.getAll(resortRoomCategoryEntity.getId(), localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryLocaleService.getActiveCount(resortRoomCategoryEntity.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryLocaleRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryLocaleService.create(request, resortRoomCategoryEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryLocaleRequest request) {
        ResortRoomCategoryLocaleEntity entity = resortRoomCategoryLocaleService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        ResortRoomCategoryLocaleEntity entity = resortRoomCategoryLocaleService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryLocaleService.delete(entity));
    }
}
