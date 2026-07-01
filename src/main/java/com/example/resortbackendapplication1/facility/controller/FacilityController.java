package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.facility.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/facility-groups/{facility-group-id}/facilities")
public class FacilityController {

    private final FacilityService facilityService;
    private final FacilityGroupService facilityGroupService;
    private final LocaleService localeService;

    public FacilityController(FacilityService facilityService,
                              FacilityGroupService facilityGroupService,
                              LocaleService localeService) {
        this.facilityService = facilityService;
        this.facilityGroupService = facilityGroupService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateFacilityRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(facilityGroupId);
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateFacilityLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityService.create(request, localeEntityMap, facilityGroupEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getById(facilityGroupId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(facilityService.getAll(facilityGroupId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityRequest request) {
        FacilityEntity entity = facilityService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(facilityService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id) {
        return ResponseEntity.ok(facilityService.delete(facilityGroupId, id));
    }
}
