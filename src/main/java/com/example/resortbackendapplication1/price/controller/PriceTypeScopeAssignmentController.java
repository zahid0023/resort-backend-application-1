package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.price.dto.request.pricetypescopeassignment.CreatePriceTypeScopeAssignmentRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeAssignmentService;
import com.example.resortbackendapplication1.price.service.PriceScopeService;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-types/{price-type-id}/scope-assignments")
public class PriceTypeScopeAssignmentController {

    private final PriceScopeService priceScopeService;
    private final PriceTypeService priceTypeService;
    private final PriceTypeScopeAssignmentService priceTypeScopeAssignmentService;

    public PriceTypeScopeAssignmentController(PriceScopeService priceScopeService,
                                               PriceTypeService priceTypeService,
                                               PriceTypeScopeAssignmentService priceTypeScopeAssignmentService) {
        this.priceScopeService = priceScopeService;
        this.priceTypeService = priceTypeService;
        this.priceTypeScopeAssignmentService = priceTypeScopeAssignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("price-type-id") Long priceTypeId,
            @Valid @RequestBody CreatePriceTypeScopeAssignmentRequest request) {
        PriceTypeEntity priceTypeEntity = priceTypeService.getEntityById(priceTypeId);
        PriceScopeEntity priceScopeEntity = priceScopeService.getEntityById(request.getPriceScopeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceTypeScopeAssignmentService.assign(priceTypeEntity, priceScopeEntity));
    }

    @DeleteMapping("/{price-scope-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("price-type-id") Long priceTypeId,
            @PathVariable("price-scope-id") Long priceScopeId) {
        PriceTypeScopeAssignmentEntity entity = priceTypeScopeAssignmentService.getEntityByPriceScopeId(priceTypeId, priceScopeId);
        return ResponseEntity.ok(priceTypeScopeAssignmentService.unassign(entity));
    }
}
