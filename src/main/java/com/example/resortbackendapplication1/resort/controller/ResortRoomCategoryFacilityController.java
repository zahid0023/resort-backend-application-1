package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.CreateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.UpdateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityGroupService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/facilities")
public class ResortRoomCategoryFacilityController {

    private final ResortRoomCategoryFacilityService resortRoomCategoryFacilityService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomCategoryFacilityGroupService resortRoomCategoryFacilityGroupService;
    private final FacilityService facilityService;
    private final LocaleService localeService;

    public ResortRoomCategoryFacilityController(ResortRoomCategoryFacilityService resortRoomCategoryFacilityService,
                                                ResortRoomCategoryService resortRoomCategoryService,
                                                ResortRoomCategoryFacilityGroupService resortRoomCategoryFacilityGroupService,
                                                FacilityService facilityService,
                                                LocaleService localeService) {
        this.resortRoomCategoryFacilityService = resortRoomCategoryFacilityService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomCategoryFacilityGroupService = resortRoomCategoryFacilityGroupService;
        this.facilityService = facilityService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(roomCategoryId, request.getResortRoomCategoryFacilityGroupId());
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryFacilityService.create(request, resortRoomCategoryEntity, resortRoomCategoryFacilityGroupEntity,
                        facilityEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryFacilityService.getById(roomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @ParameterObject ResortRoomCategoryFacilityFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.getAll(roomCategoryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityRequest request) {
        ResortRoomCategoryFacilityEntity entity = resortRoomCategoryFacilityService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        ResortRoomCategoryFacilityEntity entity = resortRoomCategoryFacilityService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.delete(entity));
    }
}
