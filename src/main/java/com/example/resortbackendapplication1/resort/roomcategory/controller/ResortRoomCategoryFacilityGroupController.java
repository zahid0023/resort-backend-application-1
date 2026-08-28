package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.CreateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.UpdateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryFacilityGroupService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/facility-groups")
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
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityGroupRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
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
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.getById(resortRoomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @ParameterObject ResortRoomCategoryFacilityGroupFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.getAll(resortRoomCategoryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityGroupRequest request) {
        ResortRoomCategoryFacilityGroupEntity entity = resortRoomCategoryFacilityGroupService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        ResortRoomCategoryFacilityGroupEntity entity = resortRoomCategoryFacilityGroupService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityGroupService.delete(entity));
    }
}
