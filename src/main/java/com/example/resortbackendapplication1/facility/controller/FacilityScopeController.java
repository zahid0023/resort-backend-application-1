package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.facility.dto.request.facilityscope.CreateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-scopes")
public class FacilityScopeController {

    private final FacilityScopeService facilityScopeService;
    private final LocaleService localeService;

    public FacilityScopeController(FacilityScopeService facilityScopeService,
                                   LocaleService localeService) {
        this.facilityScopeService = facilityScopeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateFacilityScopeRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityScopeService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityScopeService.getById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount() {
        return ResponseEntity.ok(facilityScopeService.getActiveCount());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        FacilityScopeEntity entity = facilityScopeService.getEntityById(id);
        return ResponseEntity.ok(facilityScopeService.delete(entity));
    }
}
