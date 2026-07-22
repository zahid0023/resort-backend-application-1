package com.example.resortbackendapplication1.unittype.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.CreateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.UpdateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeLocaleEntity;
import com.example.resortbackendapplication1.unittype.service.UnitTypeLocaleService;
import com.example.resortbackendapplication1.unittype.service.UnitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/unit-types/{unit-type-id}/locales")
public class UnitTypeLocaleController {

    private final UnitTypeService unitTypeService;
    private final UnitTypeLocaleService unitTypeLocaleService;
    private final LocaleService localeService;

    public UnitTypeLocaleController(UnitTypeService unitTypeService,
                                    UnitTypeLocaleService unitTypeLocaleService,
                                    LocaleService localeService) {
        this.unitTypeService = unitTypeService;
        this.unitTypeLocaleService = unitTypeLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("unit-type-id") Long unitTypeId,
            @Valid @RequestBody CreateUnitTypeLocaleRequest request) {
        UnitTypeEntity unitType = unitTypeService.getEntityById(unitTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(unitTypeLocaleService.create(unitType, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("unit-type-id") Long unitTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateUnitTypeLocaleRequest request) {
        UnitTypeLocaleEntity entity = unitTypeLocaleService.getEntityById(unitTypeId, id);
        return ResponseEntity.ok(unitTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("unit-type-id") Long unitTypeId,
            @PathVariable Long id) {
        UnitTypeLocaleEntity entity = unitTypeLocaleService.getEntityById(unitTypeId, id);
        return ResponseEntity.ok(unitTypeLocaleService.delete(entity));
    }
}
