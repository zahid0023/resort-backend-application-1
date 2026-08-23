package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.CreateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.UpdateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityGroupLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityGroupService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/facility-groups/{facility-group-id}/locales")
public class ResortRoomCategoryFacilityGroupLocaleController {

    private final ResortRoomCategoryFacilityGroupService resortRoomCategoryFacilityGroupService;
    private final ResortRoomCategoryFacilityGroupLocaleService resortRoomCategoryFacilityGroupLocaleService;
    private final LocaleService localeService;

    public ResortRoomCategoryFacilityGroupLocaleController(ResortRoomCategoryFacilityGroupService resortRoomCategoryFacilityGroupService,
                                                            ResortRoomCategoryFacilityGroupLocaleService resortRoomCategoryFacilityGroupLocaleService,
                                                            LocaleService localeService) {
        this.resortRoomCategoryFacilityGroupService = resortRoomCategoryFacilityGroupService;
        this.resortRoomCategoryFacilityGroupLocaleService = resortRoomCategoryFacilityGroupLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(roomCategoryId, facilityGroupId);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.getAll(resortRoomCategoryFacilityGroupEntity.getId(), localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityGroupLocaleRequest request) {
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(roomCategoryId, facilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryFacilityGroupLocaleService.create(request, resortRoomCategoryFacilityGroupEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityGroupLocaleRequest request) {
        ResortRoomCategoryFacilityGroupLocaleEntity entity = resortRoomCategoryFacilityGroupLocaleService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id) {
        ResortRoomCategoryFacilityGroupLocaleEntity entity = resortRoomCategoryFacilityGroupLocaleService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.delete(entity));
    }
}
