package com.example.resortbackendapplication1.resort.facility.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facility-groups")
public class ResortFacilityGroupController {

    private final ResortFacilityGroupService resortFacilityGroupService;
    private final ResortService resortService;
    private final FacilityGroupService facilityGroupService;
    private final LocaleService localeService;

    public ResortFacilityGroupController(ResortFacilityGroupService resortFacilityGroupService,
                                         ResortService resortService,
                                         FacilityGroupService facilityGroupService,
                                         LocaleService localeService) {
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.resortService = resortService;
        this.facilityGroupService = facilityGroupService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortFacilityGroupRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        FacilityGroupEntity facilityGroupEntity = request.getFacilityGroupId() != null
                ? facilityGroupService.getEntityById(request.getFacilityGroupId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityGroupService.create(request, resortEntity, facilityGroupEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityGroupService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortFacilityGroupFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortFacilityGroupService.getAll(resortId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityGroupRequest request) {
        ResortFacilityGroupEntity entity = resortFacilityGroupService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortFacilityGroupService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortFacilityGroupEntity entity = resortFacilityGroupService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortFacilityGroupService.delete(entity));
    }
}
