package com.example.resortbackendapplication1.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.CreateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.facility.service.FacilityScopeLocaleService;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-scopes/{facility-scope-id}/locales")
public class FacilityScopeLocaleController {

    private final FacilityScopeService facilityScopeService;
    private final FacilityScopeLocaleService facilityScopeLocaleService;
    private final LocaleService localeService;

    public FacilityScopeLocaleController(FacilityScopeService facilityScopeService,
                                         FacilityScopeLocaleService facilityScopeLocaleService,
                                         LocaleService localeService) {
        this.facilityScopeService = facilityScopeService;
        this.facilityScopeLocaleService = facilityScopeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        facilityScopeService.getEntityById(facilityScopeId);
        return ResponseEntity.ok(facilityScopeLocaleService.getAll(facilityScopeId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("facility-scope-id") Long facilityScopeId) {
        facilityScopeService.getEntityById(facilityScopeId);
        return ResponseEntity.ok(facilityScopeLocaleService.getCount(facilityScopeId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @Valid @RequestBody CreateFacilityScopeLocaleRequest request) {
        FacilityScopeEntity facilityScopeEntity = facilityScopeService.getEntityById(facilityScopeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityScopeLocaleService.create(request, facilityScopeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityScopeLocaleRequest request) {
        FacilityScopeLocaleEntity entity = facilityScopeLocaleService.getEntityById(facilityScopeId, id);
        return ResponseEntity.ok(facilityScopeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("facility-scope-id") Long facilityScopeId,
            @PathVariable Long id) {
        FacilityScopeLocaleEntity entity = facilityScopeLocaleService.getEntityById(facilityScopeId, id);
        return ResponseEntity.ok(facilityScopeLocaleService.delete(entity));
    }
}
