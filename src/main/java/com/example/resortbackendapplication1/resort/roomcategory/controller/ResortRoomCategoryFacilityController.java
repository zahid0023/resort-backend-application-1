package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.CreateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.UpdateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryFacilityGroupService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryFacilityService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/facilities")
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
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryFacilityRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity =
                resortRoomCategoryFacilityGroupService.getEntityById(resortRoomCategoryId, request.getResortRoomCategoryFacilityGroupId());
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
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.getById(resortRoomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @ParameterObject ResortRoomCategoryFacilityFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.getAll(resortRoomCategoryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryFacilityRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryFacilityEntity entity = resortRoomCategoryFacilityService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryFacilityEntity entity = resortRoomCategoryFacilityService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryFacilityService.delete(entity));
    }
}
