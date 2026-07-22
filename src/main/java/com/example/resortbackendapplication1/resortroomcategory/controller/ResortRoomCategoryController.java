package com.example.resortbackendapplication1.resortroomcategory.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories")
public class ResortRoomCategoryController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortService resortService;
    private final RoomCategoryService roomCategoryService;
    private final LocaleService localeService;
    private final BedTypeService bedTypeService;
    private final UnitService unitService;

    public ResortRoomCategoryController(ResortRoomCategoryService resortRoomCategoryService,
                                         ResortService resortService,
                                         RoomCategoryService roomCategoryService,
                                         LocaleService localeService,
                                         BedTypeService bedTypeService,
                                         UnitService unitService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortService = resortService;
        this.roomCategoryService = roomCategoryService;
        this.localeService = localeService;
        this.bedTypeService = bedTypeService;
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortRoomCategoryRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        RoomCategoryEntity roomCategoryEntity = roomCategoryService.getEntityById(request.getRoomCategoryId());
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortRoomCategoryLocaleRequest::getLocaleId, localeService);
        Map<Long, BedTypeEntity> bedTypeEntityMap = resolveBedTypeMap(request.getBeds());
        UnitEntity roomSizeUnit = request.getMeta().getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getMeta().getRoomSizeUnitId())
                : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryService.create(request, resortEntity, roomCategoryEntity, localeEntityMap, bedTypeEntityMap, roomSizeUnit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortRoomCategoryFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortRoomCategoryService.getAll(request, resortId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryRequest request) {
        ResortRoomCategoryEntity entity = resortRoomCategoryService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortRoomCategoryService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortRoomCategoryEntity entity = resortRoomCategoryService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortRoomCategoryService.delete(entity));
    }

    private Map<Long, BedTypeEntity> resolveBedTypeMap(List<ResortRoomCategoryBedRequest> beds) {
        if (beds == null || beds.isEmpty()) return Collections.emptyMap();
        Set<Long> bedTypeIds = beds.stream()
                .map(ResortRoomCategoryBedRequest::getBedTypeId)
                .collect(Collectors.toSet());
        return bedTypeService.getAll(bedTypeIds).stream()
                .collect(Collectors.toMap(BedTypeEntity::getId, Function.identity()));
    }
}
