package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityLocaleService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facilities/{facility-id}/locales")
public class FacilityLocaleController {

    private final FacilityService facilityService;
    private final FacilityLocaleService facilityLocaleService;
    private final LocaleService localeService;

    public FacilityLocaleController(FacilityService facilityService,
                                    FacilityLocaleService facilityLocaleService,
                                    LocaleService localeService) {
        this.facilityService = facilityService;
        this.facilityLocaleService = facilityLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-id") Long facilityId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityService.getEntityById(facilityId);
        return ResponseEntity.ok(facilityLocaleService.getAll(facilityId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody CreateFacilityLocaleRequest request) {
        FacilityEntity facilityEntity = facilityService.getEntityById(facilityId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityLocaleService.create(request, facilityEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("facility-id") Long facilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityLocaleRequest request) {
        FacilityLocaleEntity entity = facilityLocaleService.getEntityById(facilityId, id);
        return ResponseEntity.ok(facilityLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("facility-id") Long facilityId,
            @PathVariable Long id) {
        FacilityLocaleEntity entity = facilityLocaleService.getEntityById(facilityId, id);
        return ResponseEntity.ok(facilityLocaleService.delete(entity));
    }
}
