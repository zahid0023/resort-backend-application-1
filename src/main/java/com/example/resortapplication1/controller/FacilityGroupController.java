package com.example.resortapplication1.controller;

import com.example.resortapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortapplication1.service.FacilityGroupService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/facility-groups")
public class FacilityGroupController {

    private final FacilityGroupService facilityGroupService;

    public FacilityGroupController(FacilityGroupService facilityGroupService) {
        this.facilityGroupService = facilityGroupService;
    }

    @PostMapping
    public ResponseEntity<?> createFacilityGroup(@RequestBody CreateFacilityGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityGroupService.createFacilityGroup(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacilityGroup(@PathVariable Long id) {
        return ResponseEntity.ok(facilityGroupService.getFacilityGroup(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllFacilityGroups(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "code", "name"));
        return ResponseEntity.ok(facilityGroupService.getAllFacilityGroups(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacilityGroup(@PathVariable Long id, @RequestBody UpdateFacilityGroupRequest request) {
        return ResponseEntity.ok(facilityGroupService.updateFacilityGroup(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacilityGroup(@PathVariable Long id) {
        return ResponseEntity.ok(facilityGroupService.deleteFacilityGroup(id));
    }
}
