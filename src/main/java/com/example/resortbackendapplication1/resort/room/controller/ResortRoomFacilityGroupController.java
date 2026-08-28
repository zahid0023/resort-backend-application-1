package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.CreateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.ResortRoomFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.UpdateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityGroupService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups")
public class ResortRoomFacilityGroupController {

    private final ResortRoomFacilityGroupService resortRoomFacilityGroupService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final FacilityGroupService facilityGroupService;
    private final LocaleService localeService;

    public ResortRoomFacilityGroupController(ResortRoomFacilityGroupService resortRoomFacilityGroupService,
                                              ResortRoomCategoryService resortRoomCategoryService,
                                              ResortRoomService resortRoomService,
                                              FacilityGroupService facilityGroupService,
                                              LocaleService localeService) {
        this.resortRoomFacilityGroupService = resortRoomFacilityGroupService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.facilityGroupService = facilityGroupService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomFacilityGroupRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        FacilityGroupEntity facilityGroupEntity = request.getFacilityGroupId() != null
                ? facilityGroupService.getEntityById(request.getFacilityGroupId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomFacilityGroupService.create(request, resortRoomEntity, facilityGroupEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomFacilityGroupService.getById(resortRoomId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @ParameterObject ResortRoomFacilityGroupFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        return ResponseEntity.ok(resortRoomFacilityGroupService.getAll(resortRoomId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomFacilityGroupRequest request) {
        ResortRoomFacilityGroupEntity entity = resortRoomFacilityGroupService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomFacilityGroupService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        ResortRoomFacilityGroupEntity entity = resortRoomFacilityGroupService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomFacilityGroupService.delete(entity));
    }
}
