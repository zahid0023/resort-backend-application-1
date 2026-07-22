package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-scopes")
public class FacilityScopeController {

    private final FacilityScopeService facilityScopeService;

    public FacilityScopeController(FacilityScopeService facilityScopeService) {
        this.facilityScopeService = facilityScopeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityScopeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject FacilityScopeFilterRequest request) {
        return ResponseEntity.ok(facilityScopeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityScopeRequest request) {
        FacilityScopeEntity entity = facilityScopeService.getEntityById(id);
        return ResponseEntity.ok(facilityScopeService.update(entity, request));
    }
}
