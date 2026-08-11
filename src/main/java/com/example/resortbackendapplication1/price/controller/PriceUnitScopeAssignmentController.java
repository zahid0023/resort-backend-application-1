package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.price.dto.request.priceunitscopeassignment.CreatePriceUnitScopeAssignmentRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitScopeAssignmentService;
import com.example.resortbackendapplication1.price.service.PriceScopeService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-units/{price-unit-id}/scope-assignments")
public class PriceUnitScopeAssignmentController {

    private final PriceScopeService priceScopeService;
    private final PriceUnitService priceUnitService;
    private final PriceUnitScopeAssignmentService priceUnitScopeAssignmentService;

    public PriceUnitScopeAssignmentController(PriceScopeService priceScopeService,
                                               PriceUnitService priceUnitService,
                                               PriceUnitScopeAssignmentService priceUnitScopeAssignmentService) {
        this.priceScopeService = priceScopeService;
        this.priceUnitService = priceUnitService;
        this.priceUnitScopeAssignmentService = priceUnitScopeAssignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("price-unit-id") Long priceUnitId,
            @Valid @RequestBody CreatePriceUnitScopeAssignmentRequest request) {
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(priceUnitId);
        PriceScopeEntity priceScopeEntity = priceScopeService.getEntityById(request.getPriceScopeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceUnitScopeAssignmentService.assign(priceUnitEntity, priceScopeEntity));
    }

    @DeleteMapping("/{price-scope-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("price-unit-id") Long priceUnitId,
            @PathVariable("price-scope-id") Long priceScopeId) {
        PriceUnitScopeAssignmentEntity entity = priceUnitScopeAssignmentService.getEntityByPriceScopeId(priceUnitId, priceScopeId);
        return ResponseEntity.ok(priceUnitScopeAssignmentService.unassign(entity));
    }
}
