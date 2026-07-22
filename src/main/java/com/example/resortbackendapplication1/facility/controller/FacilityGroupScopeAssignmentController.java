package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroupscopeassignments.AssignFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupScopeAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-groups/{facility-group-id}/scope-assignments")
public class FacilityGroupScopeAssignmentController {

    private final FacilityGroupService facilityGroupService;
    private final FacilityScopeService facilityScopeService;
    private final FacilityGroupScopeAssignmentService assignmentService;

    public FacilityGroupScopeAssignmentController(FacilityGroupService facilityGroupService,
                                                   FacilityScopeService facilityScopeService,
                                                   FacilityGroupScopeAssignmentService assignmentService) {
        this.facilityGroupService = facilityGroupService;
        this.facilityScopeService = facilityScopeService;
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody AssignFacilityScopeRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(facilityGroupId);
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(request.getFacilityScopeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.assign(facilityGroupEntity, facilityScopeEntity));
    }

    @DeleteMapping("/{facility-scope-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable("facility-scope-id") Long facilityScopeId) {
        return ResponseEntity.ok(assignmentService.unassign(facilityGroupId, facilityScopeId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-group-id") Long facilityGroupId) {
        facilityGroupService.getEntityById(facilityGroupId);
        return ResponseEntity.ok(assignmentService.getAll(facilityGroupId));
    }
}
