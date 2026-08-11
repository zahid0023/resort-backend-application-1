package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroupscopeassignment.CreateFacilityGroupScopeAssignmentRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
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
    private final FacilityGroupScopeAssignmentService facilityGroupScopeAssignmentService;

    public FacilityGroupScopeAssignmentController(FacilityGroupService facilityGroupService,
                                                   FacilityScopeService facilityScopeService,
                                                   FacilityGroupScopeAssignmentService facilityGroupScopeAssignmentService) {
        this.facilityGroupService = facilityGroupService;
        this.facilityScopeService = facilityScopeService;
        this.facilityGroupScopeAssignmentService = facilityGroupScopeAssignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateFacilityGroupScopeAssignmentRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(facilityGroupId);
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(request.getFacilityScopeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityGroupScopeAssignmentService.assign(facilityGroupEntity, facilityScopeEntity));
    }

    @DeleteMapping("/{facility-scope-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable("facility-scope-id") Long facilityScopeId) {
        FacilityGroupScopeAssignmentEntity entity = facilityGroupScopeAssignmentService.getEntityByFacilityScopeId(facilityGroupId, facilityScopeId);
        return ResponseEntity.ok(facilityGroupScopeAssignmentService.unassign(entity));
    }
}
