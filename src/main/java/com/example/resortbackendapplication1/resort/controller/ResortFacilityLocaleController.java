package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales")
public class ResortFacilityLocaleController {

    private final ResortFacilityService resortFacilityService;
    private final ResortFacilityLocaleService resortFacilityLocaleService;
    private final LocaleService localeService;

    public ResortFacilityLocaleController(ResortFacilityService resortFacilityService,
                                          ResortFacilityLocaleService resortFacilityLocaleService,
                                          LocaleService localeService) {
        this.resortFacilityService = resortFacilityService;
        this.resortFacilityLocaleService = resortFacilityLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @Valid @RequestBody CreateResortFacilityLocaleRequest request) {
        ResortFacilityEntity resortFacility = resortFacilityService.getEntityById(resortFacilityId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityLocaleService.create(resortFacility, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityLocaleRequest request) {
        ResortFacilityLocaleEntity entity = resortFacilityLocaleService.getEntityById(resortFacilityId, id);
        return ResponseEntity.ok(resortFacilityLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id) {
        ResortFacilityLocaleEntity entity = resortFacilityLocaleService.getEntityById(resortFacilityId, id);
        return ResponseEntity.ok(resortFacilityLocaleService.delete(entity));
    }
}
