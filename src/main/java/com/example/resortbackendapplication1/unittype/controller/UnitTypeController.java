package com.example.resortbackendapplication1.unittype.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.CreateUnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UnitTypeFilterRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UpdateUnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.CreateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;
import com.example.resortbackendapplication1.unittype.service.UnitTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/unit-types")
public class UnitTypeController {

    private final UnitTypeService unitTypeService;
    private final LocaleService localeService;

    public UnitTypeController(UnitTypeService unitTypeService,
                              LocaleService localeService) {
        this.unitTypeService = unitTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUnitTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateUnitTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(unitTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(unitTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject UnitTypeFilterRequest request) {
        return ResponseEntity.ok(unitTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUnitTypeRequest request) {
        UnitTypeEntity entity = unitTypeService.getEntityById(id);
        return ResponseEntity.ok(unitTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(unitTypeService.delete(id));
    }
}
