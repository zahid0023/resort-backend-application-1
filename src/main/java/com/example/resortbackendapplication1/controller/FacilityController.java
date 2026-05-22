package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.enums.IconType;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.service.FacilityGroupService;
import com.example.resortbackendapplication1.service.FacilityService;
import com.example.resortbackendapplication1.service.LocaleService;
import com.example.resortbackendapplication1.utils.LocaleUtils;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @GetMapping("/icon-types")
    public ResponseEntity<?> getAllIconTypes(
            @PathVariable("facility-group-id") Long ignoredFacilityGroupId) {
        return ResponseEntity.ok(Arrays.asList(IconType.values()));
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
