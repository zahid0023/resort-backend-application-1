package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilityfacilitygroupassignment.CreateFacilityFacilityGroupAssignmentRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityFacilityGroupAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityFacilityGroupAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facilities/{facility-id}/group-assignments")
public class FacilityFacilityGroupAssignmentController {

    private final FacilityService facilityService;
    private final FacilityGroupService facilityGroupService;
    private final FacilityFacilityGroupAssignmentService facilityFacilityGroupAssignmentService;

    public FacilityFacilityGroupAssignmentController(FacilityService facilityService,
                                                      FacilityGroupService facilityGroupService,
                                                      FacilityFacilityGroupAssignmentService facilityFacilityGroupAssignmentService) {
        this.facilityService = facilityService;
        this.facilityGroupService = facilityGroupService;
        this.facilityFacilityGroupAssignmentService = facilityFacilityGroupAssignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody CreateFacilityFacilityGroupAssignmentRequest request) {
        FacilityEntity facilityEntity = facilityService.getEntityById(facilityId);
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getEntityById(request.getFacilityGroupId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityFacilityGroupAssignmentService.assign(facilityEntity, facilityGroupEntity));
    }

    @DeleteMapping("/{facility-group-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-id") Long facilityId,
            @PathVariable("facility-group-id") Long facilityGroupId) {
        FacilityFacilityGroupAssignmentEntity entity = facilityFacilityGroupAssignmentService.getEntityByFacilityGroupId(facilityId, facilityGroupId);
        return ResponseEntity.ok(facilityFacilityGroupAssignmentService.unassign(entity));
    }
}
