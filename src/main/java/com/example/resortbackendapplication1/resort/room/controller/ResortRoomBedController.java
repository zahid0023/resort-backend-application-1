package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.ResortRoomBedFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.UpdateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomBedService;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryBedService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds")
public class ResortRoomBedController {

    private final ResortRoomBedService resortRoomBedService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomCategoryBedService resortRoomCategoryBedService;
    private final ResortRoomService resortRoomService;
    private final BedTypeService bedTypeService;

    public ResortRoomBedController(ResortRoomBedService resortRoomBedService,
                                   ResortRoomCategoryService resortRoomCategoryService,
                                   ResortRoomCategoryBedService resortRoomCategoryBedService,
                                   ResortRoomService resortRoomService,
                                   BedTypeService bedTypeService) {
        this.resortRoomBedService = resortRoomBedService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomCategoryBedService = resortRoomCategoryBedService;
        this.resortRoomService = resortRoomService;
        this.bedTypeService = bedTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomBedRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        BedTypeEntity bedTypeEntity = bedTypeService.getEntityById(request.getBedTypeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomBedService.create(request, resortRoomEntity, bedTypeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        return ResponseEntity.ok(resortRoomBedService.getById(resortRoomId, id));
    }

    /**
     * Returns the room's own bed rows if it has any, otherwise its category's beds instead (see
     * {@code inherited} on each entry). The category fallback is resolved here, not in
     * {@code ResortRoomBedServiceImpl}, since a ServiceImpl must never call another entity's Service.
     */
    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @ParameterObject ResortRoomBedFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback = resolveResortRoomCategoryBedsFallback(resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomBedService.getAll(resortRoomId, request, resortRoomCategoryBedsFallback));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomBedRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        ResortRoomBedEntity entity = resortRoomBedService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomBedService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, resortRoomId);
        ResortRoomBedEntity entity = resortRoomBedService.getEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomBedService.delete(entity));
    }

    private List<ResortRoomCategoryBedDto> resolveResortRoomCategoryBedsFallback(Long resortRoomCategoryId) {
        return resortRoomCategoryBedService.getAllActive(resortRoomCategoryId);
    }
}
