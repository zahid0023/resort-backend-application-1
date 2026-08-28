package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.CreateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomStatusRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms")
public class ResortRoomController {

    private final ResortRoomService resortRoomService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final RoomStatusService roomStatusService;
    private final LocaleService localeService;
    private final UnitService unitService;
    private final BedTypeService bedTypeService;

    public ResortRoomController(ResortRoomService resortRoomService,
                                ResortRoomCategoryService resortRoomCategoryService,
                                RoomStatusService roomStatusService,
                                LocaleService localeService,
                                UnitService unitService,
                                BedTypeService bedTypeService) {
        this.resortRoomService = resortRoomService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.roomStatusService = roomStatusService;
        this.localeService = localeService;
        this.unitService = unitService;
        this.bedTypeService = bedTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        RoomStatusEntity roomStatusEntity = roomStatusService.getEntityById(request.getRoomStatusId());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        UnitEntity roomSizeUnitEntity = request.getMeta().getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getMeta().getRoomSizeUnitId())
                : null;
        Set<Long> bedTypeIds = request.getBeds().stream()
                .map(CreateResortRoomBedRequest::getBedTypeId)
                .collect(Collectors.toSet());
        List<BedTypeEntity> bedTypeEntities = bedTypeService.getAll(bedTypeIds);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomService.create(request, resortRoomCategoryEntity, roomStatusEntity, localeEntity, roomSizeUnitEntity, bedTypeEntities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomService.getById(resortRoomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @ParameterObject ResortRoomFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomService.getAll(resortRoomCategoryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity entity = resortRoomService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomService.update(entity, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomStatusRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity entity = resortRoomService.getEntityById(resortRoomCategoryId, id);
        RoomStatusEntity roomStatusEntity = roomStatusService.getEntityById(request.getRoomStatusId());
        return ResponseEntity.ok(resortRoomService.updateStatus(entity, roomStatusEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity entity = resortRoomService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomService.delete(entity));
    }
}
