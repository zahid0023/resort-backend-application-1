package com.example.resortbackendapplication1.resortroomcategory.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryLocaleService;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryLocaleRequest request) {
        ResortRoomCategoryEntity resortRoomCategory = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryLocaleService.create(resortRoomCategory, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryLocaleRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryLocaleEntity entity = resortRoomCategoryLocaleService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryLocaleEntity entity = resortRoomCategoryLocaleService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryLocaleService.delete(entity));
    }
}
