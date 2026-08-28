package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.locale.CreateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.locale.UpdateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryFacilityGroupLocaleService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryFacilityGroupService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/facility-groups/{resort-facility-group-id}/locales")
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
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(resortRoomCategoryId, resortFacilityGroupId);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.getAll(resortRoomCategoryFacilityGroupEntity.getId(), localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId) {
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(resortRoomCategoryId, resortFacilityGroupId);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.getActiveCount(resortRoomCategoryFacilityGroupEntity.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityGroupLocaleRequest request) {
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(resortRoomCategoryId, resortFacilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryFacilityGroupLocaleService.create(request, resortRoomCategoryFacilityGroupEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityGroupLocaleRequest request) {
        ResortRoomCategoryFacilityGroupLocaleEntity entity = resortRoomCategoryFacilityGroupLocaleService.getEntityById(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id) {
        ResortRoomCategoryFacilityGroupLocaleEntity entity = resortRoomCategoryFacilityGroupLocaleService.getEntityById(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupLocaleService.delete(entity));
    }
}
