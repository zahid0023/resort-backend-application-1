package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupLocaleService;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-groups/{facility-group-id}/locales")
public class FacilityGroupLocaleController {

    private final FacilityGroupService facilityGroupService;
    private final FacilityGroupLocaleService facilityGroupLocaleService;
    private final LocaleService localeService;

    public FacilityGroupLocaleController(FacilityGroupService facilityGroupService,
                                         FacilityGroupLocaleService facilityGroupLocaleService,
                                         LocaleService localeService) {
        this.facilityGroupService = facilityGroupService;
        this.facilityGroupLocaleService = facilityGroupLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityGroupService.getEntityById(facilityGroupId);
        return ResponseEntity.ok(facilityGroupLocaleService.getAll(facilityGroupId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("facility-group-id") Long facilityGroupId) {
        facilityGroupService.getEntityById(facilityGroupId);
        return ResponseEntity.ok(facilityGroupLocaleService.getCount(facilityGroupId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateFacilityGroupLocaleRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(facilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityGroupLocaleService.create(request, facilityGroupEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityGroupLocaleRequest request) {
        FacilityGroupLocaleEntity entity = facilityGroupLocaleService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(facilityGroupLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id) {
        FacilityGroupLocaleEntity entity = facilityGroupLocaleService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(facilityGroupLocaleService.delete(entity));
    }
}
