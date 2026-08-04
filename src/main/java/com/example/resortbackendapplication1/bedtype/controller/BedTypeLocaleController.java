package com.example.resortbackendapplication1.bedtype.controller;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.UpdateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeLocaleService;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bed-types/{bed-type-id}/locales")
public class BedTypeLocaleController {

    private final BedTypeService bedTypeService;
    private final BedTypeLocaleService bedTypeLocaleService;
    private final LocaleService localeService;

    public BedTypeLocaleController(BedTypeService bedTypeService,
                                   BedTypeLocaleService bedTypeLocaleService,
                                   LocaleService localeService) {
        this.bedTypeService = bedTypeService;
        this.bedTypeLocaleService = bedTypeLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("bed-type-id") Long bedTypeId,
            @Valid @RequestBody CreateBedTypeLocaleRequest request) {
        BedTypeEntity bedTypeEntity = bedTypeService.getEntityById(bedTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bedTypeLocaleService.create(request, bedTypeEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("bed-type-id") Long bedTypeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        bedTypeService.getEntityById(bedTypeId);
        return ResponseEntity.ok(bedTypeLocaleService.getAll(bedTypeId, localeCode, paginatedRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("bed-type-id") Long bedTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateBedTypeLocaleRequest request) {
        BedTypeLocaleEntity entity = bedTypeLocaleService.getEntityById(bedTypeId, id);
        return ResponseEntity.ok(bedTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("bed-type-id") Long bedTypeId,
            @PathVariable Long id) {
        BedTypeLocaleEntity entity = bedTypeLocaleService.getEntityById(bedTypeId, id);
        return ResponseEntity.ok(bedTypeLocaleService.delete(entity));
    }
}
