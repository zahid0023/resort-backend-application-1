package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.ResortRoomCategoryBedFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryBedService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds")
public class ResortRoomCategoryBedController {

    private final ResortRoomCategoryBedService resortRoomCategoryBedService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final BedTypeService bedTypeService;

    public ResortRoomCategoryBedController(ResortRoomCategoryBedService resortRoomCategoryBedService,
                                           ResortRoomCategoryService resortRoomCategoryService,
                                           BedTypeService bedTypeService) {
        this.resortRoomCategoryBedService = resortRoomCategoryBedService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.bedTypeService = bedTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryBedRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        BedTypeEntity bedTypeEntity = bedTypeService.getEntityById(request.getBedTypeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryBedService.create(request, resortRoomCategoryEntity, bedTypeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryBedService.getById(resortRoomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @ParameterObject ResortRoomCategoryBedFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryBedService.getAll(resortRoomCategoryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryBedRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryBedEntity entity = resortRoomCategoryBedService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryBedService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryBedEntity entity = resortRoomCategoryBedService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryBedService.delete(entity));
    }
}
