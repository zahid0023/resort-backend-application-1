package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopeassignment.CreateFacilityScopeAssignmentRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityScopeAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-scopes/{facility-scope-id}/facility-assignments")
public class FacilityScopeAssignmentController {

    private final FacilityScopeService facilityScopeService;
    private final FacilityService facilityService;
    private final FacilityScopeAssignmentService facilityScopeAssignmentService;

    public FacilityScopeAssignmentController(FacilityScopeService facilityScopeService,
                                              FacilityService facilityService,
                                              FacilityScopeAssignmentService facilityScopeAssignmentService) {
        this.facilityScopeService = facilityScopeService;
        this.facilityService = facilityService;
        this.facilityScopeAssignmentService = facilityScopeAssignmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityScopeService.getEntityById(facilityScopeId);
        return ResponseEntity.ok(facilityScopeAssignmentService.getAll(facilityScopeId, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @Valid @RequestBody CreateFacilityScopeAssignmentRequest request) {
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(facilityScopeId);
        FacilityEntity facilityEntity = facilityService.getEntityById(request.getFacilityId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityScopeAssignmentService.assign(facilityScopeEntity, facilityEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @PathVariable Long id) {
        FacilityScopeAssignmentEntity entity = facilityScopeAssignmentService.getEntityById(facilityScopeId, id);
        return ResponseEntity.ok(facilityScopeAssignmentService.unassign(entity));
    }
}
