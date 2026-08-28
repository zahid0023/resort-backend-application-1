package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomMetaService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/meta")
public class ResortRoomMetaController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final ResortRoomMetaService resortRoomMetaService;
    private final UnitService unitService;

    public ResortRoomMetaController(ResortRoomCategoryService resortRoomCategoryService,
                                    ResortRoomService resortRoomService,
                                    ResortRoomMetaService resortRoomMetaService,
                                    UnitService unitService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.resortRoomMetaService = resortRoomMetaService;
        this.unitService = unitService;
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody UpdateResortRoomMetaRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        ResortRoomMetaEntity entity = resortRoomMetaService.getEntityByResortRoomId(resortRoomId);
        UnitEntity roomSizeUnitEntity = request.getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getRoomSizeUnitId())
                : null;
        return ResponseEntity.ok(resortRoomMetaService.update(entity, request, roomSizeUnitEntity));
    }
}
