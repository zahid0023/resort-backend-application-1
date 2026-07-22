package com.example.resortbackendapplication1.bedtype.controller;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeFilterRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.CreateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.UpdateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bed-types")
public class BedTypeController {

    private final BedTypeService bedTypeService;
    private final LocaleService localeService;

    public BedTypeController(BedTypeService bedTypeService,
                             LocaleService localeService) {
        this.bedTypeService = bedTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBedTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateBedTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(bedTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bedTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject BedTypeFilterRequest request) {
        return ResponseEntity.ok(bedTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBedTypeRequest request) {
        BedTypeEntity entity = bedTypeService.getEntityById(id);
        return ResponseEntity.ok(bedTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(bedTypeService.delete(id));
    }
}
