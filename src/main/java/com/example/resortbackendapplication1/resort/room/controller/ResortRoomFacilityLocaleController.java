package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.CreateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.UpdateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityLocaleService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales")
public class ResortRoomFacilityLocaleController {

    private final ResortRoomFacilityService resortRoomFacilityService;
    private final ResortRoomFacilityLocaleService resortRoomFacilityLocaleService;
    private final LocaleService localeService;

    public ResortRoomFacilityLocaleController(ResortRoomFacilityService resortRoomFacilityService,
                                               ResortRoomFacilityLocaleService resortRoomFacilityLocaleService,
                                               LocaleService localeService) {
        this.resortRoomFacilityService = resortRoomFacilityService;
        this.resortRoomFacilityLocaleService = resortRoomFacilityLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-room-facility-id") Long resortRoomFacilityId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortRoomFacilityEntity resortRoomFacilityEntity =
                resortRoomFacilityService.getEntityById(resortRoomId, resortRoomFacilityId);
        return ResponseEntity.ok(resortRoomFacilityLocaleService.getAll(resortRoomFacilityEntity.getId(), localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-room-facility-id") Long resortRoomFacilityId) {
        ResortRoomFacilityEntity resortRoomFacilityEntity =
                resortRoomFacilityService.getEntityById(resortRoomId, resortRoomFacilityId);
        return ResponseEntity.ok(resortRoomFacilityLocaleService.getActiveCount(resortRoomFacilityEntity.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-room-facility-id") Long resortRoomFacilityId,
            @Valid @RequestBody CreateResortRoomFacilityLocaleRequest request) {
        ResortRoomFacilityEntity resortRoomFacilityEntity =
                resortRoomFacilityService.getEntityById(resortRoomId, resortRoomFacilityId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomFacilityLocaleService.create(request, resortRoomFacilityEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-room-facility-id") Long resortRoomFacilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomFacilityLocaleRequest request) {
        ResortRoomFacilityLocaleEntity entity = resortRoomFacilityLocaleService.getEntityById(resortRoomFacilityId, id);
        return ResponseEntity.ok(resortRoomFacilityLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-room-facility-id") Long resortRoomFacilityId,
            @PathVariable Long id) {
        ResortRoomFacilityLocaleEntity entity = resortRoomFacilityLocaleService.getEntityById(resortRoomFacilityId, id);
        return ResponseEntity.ok(resortRoomFacilityLocaleService.delete(entity));
    }
}
