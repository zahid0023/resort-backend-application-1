package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.service.FacilityPriceTypeLocaleService;
import com.example.resortbackendapplication1.price.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-price-types/{facility-price-type-id}/locales")
public class FacilityPriceTypeLocaleController {

    private final FacilityPriceTypeService facilityPriceTypeService;
    private final FacilityPriceTypeLocaleService facilityPriceTypeLocaleService;
    private final LocaleService localeService;

    public FacilityPriceTypeLocaleController(FacilityPriceTypeService facilityPriceTypeService,
                                                    FacilityPriceTypeLocaleService facilityPriceTypeLocaleService,
                                                    LocaleService localeService) {
        this.facilityPriceTypeService = facilityPriceTypeService;
        this.facilityPriceTypeLocaleService = facilityPriceTypeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityPriceTypeService.getEntityById(facilityPriceTypeId);
        return ResponseEntity.ok(facilityPriceTypeLocaleService.getAll(facilityPriceTypeId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("facility-price-type-id") Long facilityPriceTypeId) {
        facilityPriceTypeService.getEntityById(facilityPriceTypeId);
        return ResponseEntity.ok(facilityPriceTypeLocaleService.getCount(facilityPriceTypeId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @Valid @RequestBody CreateFacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeEntity facilityPriceTypeEntity = facilityPriceTypeService.getEntityById(facilityPriceTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityPriceTypeLocaleService.create(request, facilityPriceTypeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeLocaleEntity entity = facilityPriceTypeLocaleService.getEntityById(facilityPriceTypeId, id);
        return ResponseEntity.ok(facilityPriceTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @PathVariable Long id) {
        FacilityPriceTypeLocaleEntity entity = facilityPriceTypeLocaleService.getEntityById(facilityPriceTypeId, id);
        return ResponseEntity.ok(facilityPriceTypeLocaleService.delete(entity));
    }
}
