package com.example.resortbackendapplication1.resortroomcategoryprice.controller;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceFilterRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.service.ResortRoomCategoryPriceService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices")
public class ResortRoomCategoryPriceController {

    private final ResortRoomCategoryPriceService resortRoomCategoryPriceService;
    private final ResortService resortService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final PriceTypeService priceTypeService;
    private final PriceUnitService priceUnitService;

    public ResortRoomCategoryPriceController(ResortRoomCategoryPriceService resortRoomCategoryPriceService,
                                              ResortService resortService,
                                              ResortRoomCategoryService resortRoomCategoryService,
                                              PriceTypeService priceTypeService,
                                              PriceUnitService priceUnitService) {
        this.resortRoomCategoryPriceService = resortRoomCategoryPriceService;
        this.resortService = resortService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.priceTypeService = priceTypeService;
        this.priceUnitService = priceUnitService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategory = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        PriceTypeEntity priceType = priceTypeService.getEntityById(request.getPriceTypeId());
        PriceUnitEntity priceUnit = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryPriceService.create(request, resortRoomCategory, priceType, priceUnit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getById(resortRoomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @ParameterObject ResortRoomCategoryPriceFilterRequest request) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getAll(request, resortRoomCategoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryPriceRequest request) {
        ResortRoomCategoryPriceEntity entity = resortRoomCategoryPriceService.getEntityById(resortRoomCategoryId, id);
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        PriceTypeEntity priceType = priceTypeService.getEntityById(request.getPriceTypeId());
        PriceUnitEntity priceUnit = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.ok(resortRoomCategoryPriceService.update(entity, request, priceType, priceUnit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomCategoryPriceEntity entity = resortRoomCategoryPriceService.getEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryPriceService.delete(entity));
    }
}
