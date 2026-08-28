package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.CreateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.UpdateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityGroupLocaleService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityGroupService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales")
public class ResortRoomFacilityGroupLocaleController {

    private final ResortRoomFacilityGroupService resortRoomFacilityGroupService;
    private final ResortRoomFacilityGroupLocaleService resortRoomFacilityGroupLocaleService;
    private final LocaleService localeService;

    public ResortRoomFacilityGroupLocaleController(ResortRoomFacilityGroupService resortRoomFacilityGroupService,
                                                    ResortRoomFacilityGroupLocaleService resortRoomFacilityGroupLocaleService,
                                                    LocaleService localeService) {
        this.resortRoomFacilityGroupService = resortRoomFacilityGroupService;
        this.resortRoomFacilityGroupLocaleService = resortRoomFacilityGroupLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity =
                resortRoomFacilityGroupService.getEntityById(resortRoomId, resortFacilityGroupId);
        return ResponseEntity.ok(resortRoomFacilityGroupLocaleService.getAll(resortRoomFacilityGroupEntity.getId(), localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId) {
        ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity =
                resortRoomFacilityGroupService.getEntityById(resortRoomId, resortFacilityGroupId);
        return ResponseEntity.ok(resortRoomFacilityGroupLocaleService.getActiveCount(resortRoomFacilityGroupEntity.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @Valid @RequestBody CreateResortRoomFacilityGroupLocaleRequest request) {
        ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity =
                resortRoomFacilityGroupService.getEntityById(resortRoomId, resortFacilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomFacilityGroupLocaleService.create(request, resortRoomFacilityGroupEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomFacilityGroupLocaleRequest request) {
        ResortRoomFacilityGroupLocaleEntity entity = resortRoomFacilityGroupLocaleService.getEntityById(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortRoomFacilityGroupLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id) {
        ResortRoomFacilityGroupLocaleEntity entity = resortRoomFacilityGroupLocaleService.getEntityById(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortRoomFacilityGroupLocaleService.delete(entity));
    }
}
