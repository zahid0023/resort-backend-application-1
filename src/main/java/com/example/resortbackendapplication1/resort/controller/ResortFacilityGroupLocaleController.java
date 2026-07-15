package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales")
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

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @Valid @RequestBody CreateResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupEntity resortFacilityGroup = resortFacilityGroupService.getEntityById(resortFacilityGroupId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityGroupLocaleService.create(resortFacilityGroup, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupLocaleEntity entity = resortFacilityGroupLocaleService.getEntityById(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortFacilityGroupLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-group-id") Long resortFacilityGroupId,
            @PathVariable Long id) {
        ResortFacilityGroupLocaleEntity entity = resortFacilityGroupLocaleService.getEntityById(resortFacilityGroupId, id);
        return ResponseEntity.ok(resortFacilityGroupLocaleService.delete(entity));
    }
}
