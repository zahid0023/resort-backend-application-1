package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescopeassignment.CreatePriceTypeScopeAssignmentRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeAssignmentService;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeService;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments")
public class PriceTypeScopeAssignmentController {

    private final PriceTypeScopeService priceTypeScopeService;
    private final PriceTypeService priceTypeService;
    private final PriceTypeScopeAssignmentService priceTypeScopeAssignmentService;

    public PriceTypeScopeAssignmentController(PriceTypeScopeService priceTypeScopeService,
                                               PriceTypeService priceTypeService,
                                               PriceTypeScopeAssignmentService priceTypeScopeAssignmentService) {
        this.priceTypeScopeService = priceTypeScopeService;
        this.priceTypeService = priceTypeService;
        this.priceTypeScopeAssignmentService = priceTypeScopeAssignmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        priceTypeScopeService.getEntityById(priceTypeScopeId);
        return ResponseEntity.ok(priceTypeScopeAssignmentService.getAll(priceTypeScopeId, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @Valid @RequestBody CreatePriceTypeScopeAssignmentRequest request) {
        PriceTypeScopeEntity priceTypeScopeEntity = priceTypeScopeService.getEntityById(priceTypeScopeId);
        PriceTypeEntity priceTypeEntity = priceTypeService.getEntityById(request.getPriceTypeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceTypeScopeAssignmentService.assign(priceTypeScopeEntity, priceTypeEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unassign(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @PathVariable Long id) {
        PriceTypeScopeAssignmentEntity entity = priceTypeScopeAssignmentService.getEntityById(priceTypeScopeId, id);
        return ResponseEntity.ok(priceTypeScopeAssignmentService.unassign(entity));
    }
}
