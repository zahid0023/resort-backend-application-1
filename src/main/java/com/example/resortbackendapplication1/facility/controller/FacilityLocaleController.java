package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityLocaleService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody CreateFacilityLocaleRequest request) {
        FacilityEntity facility = facilityService.getEntityById(facilityId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityLocaleService.create(facility, localeEntity, request));
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
