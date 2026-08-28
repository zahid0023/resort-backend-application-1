package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-price-types")
public class FacilityPriceTypeController {

    private final FacilityPriceTypeService facilityPriceTypeService;
    private final LocaleService localeService;

    public FacilityPriceTypeController(FacilityPriceTypeService facilityPriceTypeService,
                                             LocaleService localeService) {
        this.facilityPriceTypeService = facilityPriceTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityPriceTypeRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityPriceTypeService.create(request, localeEntity));
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
        FacilityPriceTypeEntity entity = facilityPriceTypeService.getEntityById(id);
        return ResponseEntity.ok(facilityPriceTypeService.delete(entity));
    }
}
