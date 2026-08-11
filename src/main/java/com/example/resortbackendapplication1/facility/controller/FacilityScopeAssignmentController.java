package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilityscopeassignment.CreateFacilityScopeAssignmentRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityScopeAssignmentService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facilities/{facility-id}/scope-assignments")
public class FacilityScopeAssignmentController {

    private final FacilityService facilityService;
    private final FacilityScopeService facilityScopeService;
    private final FacilityScopeAssignmentService facilityScopeAssignmentService;

    public FacilityScopeAssignmentController(FacilityService facilityService,
                                              FacilityScopeService facilityScopeService,
                                              FacilityScopeAssignmentService facilityScopeAssignmentService) {
        this.facilityService = facilityService;
        this.facilityScopeService = facilityScopeService;
        this.facilityScopeAssignmentService = facilityScopeAssignmentService;
    }

    @PostMapping
    public ResponseEntity<?> assign(
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody CreateFacilityScopeAssignmentRequest request) {
        FacilityEntity facilityEntity = facilityService.getEntityById(facilityId);
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(request.getFacilityScopeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityScopeAssignmentService.assign(facilityEntity, facilityScopeEntity));
    }

    @DeleteMapping("/{facility-scope-id}")
    public ResponseEntity<?> unassign(
            @PathVariable("facility-id") Long facilityId,
            @PathVariable("facility-scope-id") Long facilityScopeId) {
        FacilityScopeAssignmentEntity entity = facilityScopeAssignmentService.getEntityByFacilityScopeId(facilityId, facilityScopeId);
        return ResponseEntity.ok(facilityScopeAssignmentService.unassign(entity));
    }
}
