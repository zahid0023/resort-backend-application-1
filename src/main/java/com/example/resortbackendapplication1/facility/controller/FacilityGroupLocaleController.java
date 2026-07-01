package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupLocaleService;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateFacilityGroupLocaleRequest request) {
        FacilityGroupEntity facilityGroup = facilityGroupService.getEntityById(facilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityGroupLocaleService.create(facilityGroup, localeEntity, request));
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
