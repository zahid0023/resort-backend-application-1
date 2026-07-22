package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilityscopeassignments.AssignFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityScopeAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facilities/{facility-id}/scope-assignments")
public class FacilityScopeAssignmentController {

    private final FacilityService facilityService;
    private final FacilityScopeService facilityScopeService;
    private final FacilityScopeAssignmentService assignmentService;

    public FacilityScopeAssignmentController(FacilityService facilityService,
                                              FacilityScopeService facilityScopeService,
                                              FacilityScopeAssignmentService assignmentService) {
        this.facilityService = facilityService;
        this.facilityScopeService = facilityScopeService;
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody AssignFacilityScopeRequest request) {
        FacilityEntity facilityEntity = facilityService.getEntityById(facilityId);
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(request.getFacilityScopeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.assign(facilityEntity, facilityScopeEntity));
    }

    @DeleteMapping("/{facility-scope-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-id") Long facilityId,
            @PathVariable("facility-scope-id") Long facilityScopeId) {
        return ResponseEntity.ok(assignmentService.unassign(facilityId, facilityScopeId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable("facility-id") Long facilityId) {
        facilityService.getEntityById(facilityId);
        return ResponseEntity.ok(assignmentService.getAll(facilityId));
    }
}
