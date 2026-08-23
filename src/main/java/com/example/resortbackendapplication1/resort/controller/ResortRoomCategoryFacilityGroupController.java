package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.CreateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.UpdateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityGroupService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/facility-groups")
public class ResortRoomCategoryFacilityGroupController {

    private final ResortRoomCategoryFacilityGroupService resortRoomCategoryFacilityGroupService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final FacilityGroupService facilityGroupService;
    private final LocaleService localeService;

    public ResortRoomCategoryFacilityGroupController(ResortRoomCategoryFacilityGroupService resortRoomCategoryFacilityGroupService,
                                                      ResortRoomCategoryService resortRoomCategoryService,
                                                      FacilityGroupService facilityGroupService,
                                                      LocaleService localeService) {
        this.resortRoomCategoryFacilityGroupService = resortRoomCategoryFacilityGroupService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.facilityGroupService = facilityGroupService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityGroupRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        FacilityGroupEntity facilityGroupEntity = request.getFacilityGroupId() != null
                ? facilityGroupService.getEntityById(request.getFacilityGroupId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryFacilityGroupService.create(request, resortRoomCategoryEntity, facilityGroupEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.getById(roomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @ParameterObject ResortRoomCategoryFacilityGroupFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.getAll(roomCategoryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityGroupRequest request) {
        ResortRoomCategoryFacilityGroupEntity entity = resortRoomCategoryFacilityGroupService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        ResortRoomCategoryFacilityGroupEntity entity = resortRoomCategoryFacilityGroupService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.delete(entity));
    }
}
