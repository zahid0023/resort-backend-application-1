package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupCountRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facility-groups")
public class FacilityGroupController {

    private final FacilityGroupService facilityGroupService;
    private final FacilityScopeService facilityScopeService;
    private final LocaleService localeService;

    public FacilityGroupController(FacilityGroupService facilityGroupService,
                                   FacilityScopeService facilityScopeService,
                                   LocaleService localeService) {
        this.facilityGroupService = facilityGroupService;
        this.facilityScopeService = facilityScopeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityGroupRequest request) {
        List<FacilityScopeEntity> facilityScopeEntities = facilityScopeService.getAll(request.getFacilityScopeIds());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityGroupService.create(request, facilityScopeEntities, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityGroupService.getById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@Valid @ParameterObject FacilityGroupCountRequest request) {
        facilityScopeService.getAllByCodes(request.getScopeCodes());
        return ResponseEntity.ok(facilityGroupService.getCount(request.getScopeCodes()));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject FacilityGroupFilterRequest request) {
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
        FacilityGroupEntity entity = facilityGroupService.getEntityById(id);
        return ResponseEntity.ok(facilityGroupService.delete(entity));
    }
}
