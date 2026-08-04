package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facility.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facilities")
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
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(request.getFacilityGroupId());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.create(request, facilityGroupEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject FacilityFilterRequest request) {
        return ResponseEntity.ok(facilityService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityRequest request) {
        FacilityEntity entity = facilityService.getEntityById(id);
        return ResponseEntity.ok(facilityService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        FacilityEntity entity = facilityService.getEntityById(id);
        return ResponseEntity.ok(facilityService.delete(entity));
    }
}
