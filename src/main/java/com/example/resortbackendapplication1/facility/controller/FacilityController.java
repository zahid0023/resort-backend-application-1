package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.facility.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeCode;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;
    private final FacilityGroupService facilityGroupService;
    private final FacilityScopeService facilityScopeService;
    private final LocaleService localeService;

    public FacilityController(FacilityService facilityService,
                              FacilityGroupService facilityGroupService,
                              FacilityScopeService facilityScopeService,
                              LocaleService localeService) {
        this.facilityService = facilityService;
        this.facilityGroupService = facilityGroupService;
        this.facilityScopeService = facilityScopeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(request.getFacilityGroupId());
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateFacilityLocaleRequest::getLocaleId, localeService);
        List<FacilityScopeEntity> scopeEntities = facilityScopeService.getAll(new HashSet<>(request.getScopeIds()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityService.create(request, localeEntityMap, facilityGroupEntity, scopeEntities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject FacilityFilterRequest request,
                                    @RequestParam(name = "facility-group-id", required = false) Long facilityGroupId,
                                    @RequestParam("scope-code") FacilityScopeCode scopeCode) {
        return ResponseEntity.ok(facilityService.getAll(request, facilityGroupId, scopeCode));
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
        return ResponseEntity.ok(facilityService.delete(id));
    }
}
