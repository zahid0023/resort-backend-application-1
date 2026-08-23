package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.locale.CreateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.locale.UpdateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/facilities/{resort-room-category-facility-id}/locales")
public class ResortRoomCategoryFacilityLocaleController {

    private final ResortRoomCategoryFacilityService resortRoomCategoryFacilityService;
    private final ResortRoomCategoryFacilityLocaleService resortRoomCategoryFacilityLocaleService;
    private final LocaleService localeService;

    public ResortRoomCategoryFacilityLocaleController(ResortRoomCategoryFacilityService resortRoomCategoryFacilityService,
                                                       ResortRoomCategoryFacilityLocaleService resortRoomCategoryFacilityLocaleService,
                                                       LocaleService localeService) {
        this.resortRoomCategoryFacilityService = resortRoomCategoryFacilityService;
        this.resortRoomCategoryFacilityLocaleService = resortRoomCategoryFacilityLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("resort-room-category-facility-id") Long resortRoomCategoryFacilityId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortRoomCategoryFacilityEntity resortRoomCategoryFacilityEntity =
                resortRoomCategoryFacilityService.getEntityById(roomCategoryId, resortRoomCategoryFacilityId);
        return ResponseEntity.ok(resortRoomCategoryFacilityLocaleService.getAll(resortRoomCategoryFacilityEntity.getId(), localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("resort-room-category-facility-id") Long resortRoomCategoryFacilityId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityLocaleRequest request) {
        ResortRoomCategoryFacilityEntity resortRoomCategoryFacilityEntity =
                resortRoomCategoryFacilityService.getEntityById(roomCategoryId, resortRoomCategoryFacilityId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryFacilityLocaleService.create(request, resortRoomCategoryFacilityEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("resort-room-category-facility-id") Long resortRoomCategoryFacilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityLocaleRequest request) {
        ResortRoomCategoryFacilityLocaleEntity entity = resortRoomCategoryFacilityLocaleService.getEntityById(resortRoomCategoryFacilityId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("resort-room-category-facility-id") Long resortRoomCategoryFacilityId,
            @PathVariable Long id) {
        ResortRoomCategoryFacilityLocaleEntity entity = resortRoomCategoryFacilityLocaleService.getEntityById(resortRoomCategoryFacilityId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityLocaleService.delete(entity));
    }
}
