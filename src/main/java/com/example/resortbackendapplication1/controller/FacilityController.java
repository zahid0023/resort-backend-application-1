package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.service.FacilityService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping
    public ResponseEntity<?> createFacility(@RequestBody CreateFacilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.createFacility(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacility(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getFacility(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllFacilities(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "code", "name"));
        return ResponseEntity.ok(facilityService.getAllFacilities(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacility(@PathVariable Long id, @RequestBody UpdateFacilityRequest request) {
        return ResponseEntity.ok(facilityService.updateFacility(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacility(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.deleteFacility(id));
    }
}
