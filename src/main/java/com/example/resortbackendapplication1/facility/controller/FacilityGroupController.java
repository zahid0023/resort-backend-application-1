package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.commons.model.enums.IconType;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/facility-groups")
public class FacilityGroupController {

    private final FacilityGroupService facilityGroupService;
    private final LocaleService localeService;

    public FacilityGroupController(FacilityGroupService facilityGroupService,
                                   LocaleService localeService) {
        this.facilityGroupService = facilityGroupService;
        this.localeService = localeService;
    }

    @GetMapping("/icon-types")
    public ResponseEntity<?> getAllIconTypes() {
        return ResponseEntity.ok(Arrays.asList(IconType.values()));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityGroupRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateFacilityGroupLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityGroupService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityGroupService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(facilityGroupService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityGroupRequest request) {
        FacilityGroupEntity entity = facilityGroupService.getEntityById(id);
        return ResponseEntity.ok(facilityGroupService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(facilityGroupService.delete(id));
    }
}
