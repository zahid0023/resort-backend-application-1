package com.example.resortbackendapplication1.facilitypricetype.controller;

import com.example.resortbackendapplication1.facilitypricetype.dto.request.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.service.FacilityPriceTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-price-types")
public class FacilityPriceTypeController {

    private final FacilityPriceTypeService facilityPriceTypeService;

    public FacilityPriceTypeController(FacilityPriceTypeService facilityPriceTypeService) {
        this.facilityPriceTypeService = facilityPriceTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityPriceTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityPriceTypeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityPriceTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject FacilityPriceTypeFilterRequest request) {
        return ResponseEntity.ok(facilityPriceTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityPriceTypeRequest request) {
        FacilityPriceTypeEntity entity = facilityPriceTypeService.getEntityById(id);
        return ResponseEntity.ok(facilityPriceTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(facilityPriceTypeService.delete(id));
    }
}
