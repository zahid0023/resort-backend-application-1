package com.example.resortbackendapplication1.resortroomcategory.controller;

import com.example.resortbackendapplication1.resortroomcategory.dto.request.meta.ResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta")
public class ResortRoomCategoryMetaController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;
    private final UnitService unitService;

    public ResortRoomCategoryMetaController(ResortRoomCategoryService resortRoomCategoryService,
                                             ResortRoomCategoryMetaService resortRoomCategoryMetaService,
                                             UnitService unitService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
        this.unitService = unitService;
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody ResortRoomCategoryMetaRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryMetaEntity entity = resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(resortRoomCategoryId);
        UnitEntity roomSizeUnit = request.getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getRoomSizeUnitId())
                : null;
        return ResponseEntity.ok(resortRoomCategoryMetaService.update(entity, request, roomSizeUnit));
    }
}
