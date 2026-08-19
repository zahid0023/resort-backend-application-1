package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.locale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.locale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facility-groups/{facility-group-id}/locales")
public class ResortFacilityGroupLocaleController {

    private final ResortFacilityGroupService resortFacilityGroupService;
    private final ResortFacilityGroupLocaleService resortFacilityGroupLocaleService;
    private final LocaleService localeService;

    public ResortFacilityGroupLocaleController(ResortFacilityGroupService resortFacilityGroupService,
                                               ResortFacilityGroupLocaleService resortFacilityGroupLocaleService,
                                               LocaleService localeService) {
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.resortFacilityGroupLocaleService = resortFacilityGroupLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getEntityById(resortId, facilityGroupId);
        return ResponseEntity.ok(resortFacilityGroupLocaleService.getAll(resortFacilityGroupEntity.getId(), localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @Valid @RequestBody CreateResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getEntityById(resortId, facilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityGroupLocaleService.create(request, resortFacilityGroupEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupLocaleEntity entity = resortFacilityGroupLocaleService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(resortFacilityGroupLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-group-id") Long facilityGroupId,
            @PathVariable Long id) {
        ResortFacilityGroupLocaleEntity entity = resortFacilityGroupLocaleService.getEntityById(facilityGroupId, id);
        return ResponseEntity.ok(resortFacilityGroupLocaleService.delete(entity));
    }
}
