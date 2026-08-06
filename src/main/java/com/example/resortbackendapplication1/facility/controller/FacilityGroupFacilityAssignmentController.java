package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroupfacilityassignment.CreateFacilityGroupFacilityAssignmentRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupFacilityAssignmentEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupFacilityAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-groups/{facility-group-id}/facility-assignments")
public class FacilityGroupFacilityAssignmentController {

    private final FacilityGroupService facilityGroupService;
    private final FacilityService facilityService;
    private final FacilityGroupFacilityAssignmentService facilityGroupFacilityAssignmentService;

    public FacilityGroupFacilityAssignmentController(FacilityGroupService facilityGroupService,
                                                      FacilityService facilityService,
                                                      FacilityGroupFacilityAssignmentService facilityGroupFacilityAssignmentService) {
        this.facilityGroupService = facilityGroupService;
        this.facilityService = facilityService;
        this.facilityGroupFacilityAssignmentService = facilityGroupFacilityAssignmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityGroupService.getEntityById(facilityGroupId);
        return ResponseEntity.ok(facilityGroupFacilityAssignmentService.getAll(facilityGroupId, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateFacilityGroupFacilityAssignmentRequest request) {
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(facilityGroupId);
        FacilityEntity facilityEntity = facilityService.getEntityById(request.getFacilityId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityGroupFacilityAssignmentService.assign(facilityGroupEntity, facilityEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id) {
        FacilityGroupFacilityAssignmentEntity entity = facilityGroupFacilityAssignmentService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(facilityGroupFacilityAssignmentService.unassign(entity));
    }
}
