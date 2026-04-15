package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.service.FacilityGroupService;
import com.example.resortbackendapplication1.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.service.ResortService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-facility-groups")
public class ResortFacilityGroupController {

    private final ResortFacilityGroupService resortFacilityGroupService;
    private final ResortService resortService;
    private final FacilityGroupService facilityGroupService;

    public ResortFacilityGroupController(ResortFacilityGroupService resortFacilityGroupService,
                                         ResortService resortService,
                                         FacilityGroupService facilityGroupService) {
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.resortService = resortService;
        this.facilityGroupService = facilityGroupService;
    }

    @PostMapping
    public ResponseEntity<?> createResortFacilityGroup(@PathVariable("resort-id") Long resortId, @RequestBody CreateResortFacilityGroupRequest request) {
        ResortEntity resortEntity = resortService.getResortById(resortId);
        FacilityGroupEntity facilityGroupEntity = facilityGroupService.getFacilityGroupEntity(request.getFacilityGroupId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortFacilityGroupService.createResortFacilityGroup(request, resortEntity, facilityGroupEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResortFacilityGroup(@PathVariable("resort-id") Long resortId, @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityGroupService.getResortFacilityGroup(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllResortFacilityGroups(@PathVariable("resort-id") Long resortId, @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "name", "sortOrder"));
        return ResponseEntity.ok(resortFacilityGroupService.getAllResortFacilityGroups(resortId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortFacilityGroup(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @RequestBody UpdateResortFacilityGroupRequest request) {
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getResortFacilityGroupEntity(resortId, id);
        return ResponseEntity.ok(resortFacilityGroupService.updateResortFacilityGroup(resortFacilityGroupEntity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortFacilityGroup(@PathVariable("resort-id") Long resortId, @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityGroupService.deleteResortFacilityGroup(resortId, id));
    }
}
