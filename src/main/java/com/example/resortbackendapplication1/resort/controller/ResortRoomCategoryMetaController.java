package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.UpdateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta")
public class ResortRoomCategoryMetaController {

    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;
    private final UnitService unitService;

    public ResortRoomCategoryMetaController(ResortRoomCategoryMetaService resortRoomCategoryMetaService,
                                            UnitService unitService) {
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
        this.unitService = unitService;
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody UpdateResortRoomCategoryMetaRequest request) {
        ResortRoomCategoryMetaEntity entity = resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(resortRoomCategoryId);
        UnitEntity roomSizeUnitEntity = request.getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getRoomSizeUnitId())
                : null;
        return ResponseEntity.ok(resortRoomCategoryMetaService.update(entity, request, roomSizeUnitEntity));
    }
}
