package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.CreateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryMetaMapper;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomMetaService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/meta")
public class ResortRoomMetaController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final ResortRoomMetaService resortRoomMetaService;
    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;
    private final UnitService unitService;

    public ResortRoomMetaController(ResortRoomCategoryService resortRoomCategoryService,
                                    ResortRoomService resortRoomService,
                                    ResortRoomMetaService resortRoomMetaService,
                                    ResortRoomCategoryMetaService resortRoomCategoryMetaService,
                                    UnitService unitService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.resortRoomMetaService = resortRoomMetaService;
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
        this.unitService = unitService;
    }

    /**
     * Returns the room's own meta override if it has one, otherwise its category's meta instead (see
     * {@code inherited} on the response). The category fallback is resolved here, not in
     * {@code ResortRoomMetaServiceImpl}, since a ServiceImpl must never call another entity's Service.
     */
    @GetMapping
    public ResponseEntity<?> get(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId) {
        resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        ResortRoomCategoryMetaEntity categoryMetaEntity =
                resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(resortRoomCategoryId);
        ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback = ResortRoomCategoryMetaMapper.toDto(categoryMetaEntity).build();
        return ResponseEntity.ok(resortRoomMetaService.getByResortRoomId(resortRoomId, resortRoomCategoryMetaFallback));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomMetaRequest request) {
        ResortRoomEntity resortRoomEntity = resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        UnitEntity roomSizeUnitEntity = request.getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getRoomSizeUnitId())
                : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomMetaService.create(request, resortRoomEntity, roomSizeUnitEntity));
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody UpdateResortRoomMetaRequest request) {
        resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        ResortRoomMetaEntity entity = resortRoomMetaService.getEntityByResortRoomId(resortRoomId);
        UnitEntity roomSizeUnitEntity = request.getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getRoomSizeUnitId())
                : null;
        return ResponseEntity.ok(resortRoomMetaService.update(entity, request, roomSizeUnitEntity));
    }

    /** Reverts the room back to inheriting its category's meta. */
    @DeleteMapping
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId) {
        resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        ResortRoomMetaEntity entity = resortRoomMetaService.getEntityByResortRoomId(resortRoomId);
        return ResponseEntity.ok(resortRoomMetaService.delete(entity));
    }

    private ResortRoomEntity resolveRoom(Long resortId, Long resortRoomCategoryId, Long resortRoomId) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return resortRoomService.getEntityById(resortRoomCategoryEntity.getId(), resortRoomId);
    }
}
