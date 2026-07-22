package com.example.resortbackendapplication1.resortroomcategory.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryBedService;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds")
public class ResortRoomCategoryBedController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomCategoryBedService resortRoomCategoryBedService;
    private final BedTypeService bedTypeService;

    public ResortRoomCategoryBedController(ResortRoomCategoryService resortRoomCategoryService,
                                            ResortRoomCategoryBedService resortRoomCategoryBedService,
                                            BedTypeService bedTypeService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomCategoryBedService = resortRoomCategoryBedService;
        this.bedTypeService = bedTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryBedRequest request) {
        ResortRoomCategoryEntity resortRoomCategory = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        BedTypeEntity bedTypeEntity = bedTypeService.getEntityById(request.getBedTypeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryBedService.create(resortRoomCategory, bedTypeEntity, request));
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
