package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.service.FacilityService;
import com.example.resortbackendapplication1.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.service.ResortFacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities")
public class ResortFacilityController {

    private final ResortFacilityService resortFacilityService;
    private final ResortFacilityGroupService resortFacilityGroupService;
    private final FacilityService facilityService;

    public ResortFacilityController(ResortFacilityService resortFacilityService,
                                    ResortFacilityGroupService resortFacilityGroupService,
                                    FacilityService facilityService) {
        this.resortFacilityService = resortFacilityService;
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.facilityService = facilityService;
    }

    @PostMapping
    public ResponseEntity<?> createResortFacility(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @Valid @RequestBody CreateResortFacilityRequest request) {
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getResortFacilityGroupEntity(resortId, resortFacilityGroupId);
        FacilityEntity facilityEntity = null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.createResortFacility(request, resortFacilityGroupEntity, facilityEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResortFacility(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.getResortFacility(resortFacilityGroupId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllResortFacilities(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "name", "sortOrder"));
        return ResponseEntity.ok(resortFacilityService.getAllResortFacilities(resortFacilityGroupId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortFacility(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityRequest request) {
        ResortFacilityEntity entity = resortFacilityService.getResortFacilityEntity(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortFacilityService.updateResortFacility(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortFacility(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.deleteResortFacility(resortFacilityGroupId, id));
    }
}
