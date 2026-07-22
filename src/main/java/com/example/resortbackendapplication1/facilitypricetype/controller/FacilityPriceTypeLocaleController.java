package com.example.resortbackendapplication1.facilitypricetype.controller;

import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.facilitypricetype.service.FacilityPriceTypeLocaleService;
import com.example.resortbackendapplication1.facilitypricetype.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facility-price-types/{facility-price-type-id}/locales")
public class FacilityPriceTypeLocaleController {

    private final FacilityPriceTypeService facilityPriceTypeService;
    private final FacilityPriceTypeLocaleService facilityPriceTypeLocaleService;
    private final LocaleService localeService;

    public FacilityPriceTypeLocaleController(FacilityPriceTypeService facilityPriceTypeService,
                                             FacilityPriceTypeLocaleService facilityPriceTypeLocaleService,
                                             LocaleService localeService) {
        this.facilityPriceTypeService = facilityPriceTypeService;
        this.facilityPriceTypeLocaleService = facilityPriceTypeLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @Valid @RequestBody CreateFacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeEntity facilityPriceType = facilityPriceTypeService.getEntityById(facilityPriceTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facilityPriceTypeLocaleService.create(facilityPriceType, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeLocaleEntity entity = facilityPriceTypeLocaleService.getEntityById(facilityPriceTypeId, id);
        return ResponseEntity.ok(facilityPriceTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("facility-price-type-id") Long facilityPriceTypeId,
            @PathVariable Long id) {
        FacilityPriceTypeLocaleEntity entity = facilityPriceTypeLocaleService.getEntityById(facilityPriceTypeId, id);
        return ResponseEntity.ok(facilityPriceTypeLocaleService.delete(entity));
    }
}
