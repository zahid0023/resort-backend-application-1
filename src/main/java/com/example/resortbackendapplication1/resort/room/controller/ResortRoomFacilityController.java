package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.CreateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.ResortRoomFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.UpdateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityGroupService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities")
public class ResortRoomFacilityController {

    private final ResortRoomFacilityService resortRoomFacilityService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final ResortRoomFacilityGroupService resortRoomFacilityGroupService;
    private final FacilityService facilityService;
    private final LocaleService localeService;

    public ResortRoomFacilityController(ResortRoomFacilityService resortRoomFacilityService,
                                        ResortRoomCategoryService resortRoomCategoryService,
                                        ResortRoomService resortRoomService,
                                        ResortRoomFacilityGroupService resortRoomFacilityGroupService,
                                        FacilityService facilityService,
                                        LocaleService localeService) {
        this.resortRoomFacilityService = resortRoomFacilityService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.resortRoomFacilityGroupService = resortRoomFacilityGroupService;
        this.facilityService = facilityService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomFacilityRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity =
                resortRoomFacilityGroupService.getEntityById(resortRoomId, request.getResortRoomFacilityGroupId());
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomFacilityService.create(request, resortRoomEntity, resortRoomFacilityGroupEntity,
                        facilityEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomFacilityService.getById(resortRoomId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @ParameterObject ResortRoomFacilityFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        return ResponseEntity.ok(resortRoomFacilityService.getAll(resortRoomId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomFacilityRequest request) {
        ResortRoomFacilityEntity entity = resortRoomFacilityService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomFacilityService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        ResortRoomFacilityEntity entity = resortRoomFacilityService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomFacilityService.delete(entity));
    }
}
