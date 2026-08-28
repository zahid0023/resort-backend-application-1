package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorymeta.UpdateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMetaEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta")
public class ResortRoomCategoryMetaController {

    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;
    private final UnitService unitService;
    private final ResortRoomCategoryService resortRoomCategoryService;

    public ResortRoomCategoryMetaController(ResortRoomCategoryMetaService resortRoomCategoryMetaService,
                                            UnitService unitService,
                                            ResortRoomCategoryService resortRoomCategoryService) {
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
        this.unitService = unitService;
        this.resortRoomCategoryService = resortRoomCategoryService;
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody UpdateResortRoomCategoryMetaRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryMetaEntity entity = resortRoomCategoryEntity.getResortRoomCategoryMetaEntity();
        UnitEntity roomSizeUnitEntity = request.getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getRoomSizeUnitId())
                : null;
        return ResponseEntity.ok(resortRoomCategoryMetaService.update(entity, request, roomSizeUnitEntity));
    }
}
