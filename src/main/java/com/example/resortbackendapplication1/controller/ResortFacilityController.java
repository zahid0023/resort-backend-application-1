package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.BulkCreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.service.FacilityService;
import com.example.resortbackendapplication1.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.service.ResortFacilityService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities")
public class ResortFacilityController {

    private final ResortFacilityService resortFacilityService;
    private final FacilityService facilityService;
    private final ResortFacilityGroupService resortFacilityGroupService;

    public ResortFacilityController(ResortFacilityService resortFacilityService,
                                    FacilityService facilityService,
                                    ResortFacilityGroupService resortFacilityGroupService) {
        this.resortFacilityService = resortFacilityService;
        this.facilityService = facilityService;
        this.resortFacilityGroupService = resortFacilityGroupService;
    }

    @PostMapping
    public ResponseEntity<?> createResortFacility(
            @PathVariable("resort-id") String ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @RequestBody CreateResortFacilityRequest request) {
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getResortFacilityGroupEntity(resortFacilityGroupId);
        FacilityEntity facilityEntity = facilityService.getFacilityEntity(request.getFacilityId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.createResortFacility(request, facilityEntity, resortFacilityGroupEntity));
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulkCreateResortFacilities(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @RequestBody BulkCreateResortFacilityRequest request) {
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getResortFacilityGroupEntity(resortFacilityGroupId);
        Set<Long> facilityIds = request.getFacilities().stream()
                .map(CreateResortFacilityRequest::getFacilityId)
                .collect(Collectors.toSet());
        List<FacilityEntity> facilityEntities = facilityService.getFacilityEntities(facilityIds);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.bulkCreateResortFacilities(request, resortFacilityGroupEntity, facilityEntities));
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
        Pageable pageable = request.toPageable(Set.of("id", "name"));
        return ResponseEntity.ok(resortFacilityService.getAllResortFacilities(resortFacilityGroupId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortFacility(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id,
            @RequestBody UpdateResortFacilityRequest request) {
        ResortFacilityEntity resortFacilityEntity = resortFacilityService.getResortFacilityEntity(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortFacilityService.updateResortFacility(resortFacilityEntity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortFacility(
            @PathVariable("resort-id") Long ignoredResortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.deleteResortFacility(resortFacilityGroupId, id));
    }
}
