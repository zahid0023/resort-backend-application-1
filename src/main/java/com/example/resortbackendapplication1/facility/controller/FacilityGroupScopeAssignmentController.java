package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroupscopeassignment.CreateFacilityGroupScopeAssignmentRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupScopeAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-scopes/{facility-scope-id}/group-assignments")
public class FacilityGroupScopeAssignmentController {

    private final FacilityScopeService facilityScopeService;
    private final FacilityGroupService facilityGroupService;
    private final FacilityGroupScopeAssignmentService facilityGroupScopeAssignmentService;

    public FacilityGroupScopeAssignmentController(FacilityScopeService facilityScopeService,
                                                   FacilityGroupService facilityGroupService,
                                                   FacilityGroupScopeAssignmentService facilityGroupScopeAssignmentService) {
        this.facilityScopeService = facilityScopeService;
        this.facilityGroupService = facilityGroupService;
        this.facilityGroupScopeAssignmentService = facilityGroupScopeAssignmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityScopeService.getEntityById(facilityScopeId);
        return ResponseEntity.ok(facilityGroupScopeAssignmentService.getAll(facilityScopeId, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @Valid @RequestBody CreateFacilityGroupScopeAssignmentRequest request) {
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(facilityScopeId);
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(request.getFacilityGroupId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityGroupScopeAssignmentService.assign(facilityScopeEntity, facilityGroupEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @PathVariable Long id) {
        FacilityGroupScopeAssignmentEntity entity = facilityGroupScopeAssignmentService.getEntityById(facilityScopeId, id);
        return ResponseEntity.ok(facilityGroupScopeAssignmentService.unassign(entity));
    }
}
