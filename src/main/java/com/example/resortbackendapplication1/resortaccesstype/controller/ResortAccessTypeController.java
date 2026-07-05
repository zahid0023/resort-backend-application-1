package com.example.resortbackendapplication1.resortaccesstype.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.CreateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.ResortAccessTypeFilterRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.UpdateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.CreateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resort-access-types")
public class ResortAccessTypeController {

    private final ResortAccessTypeService resortAccessTypeService;
    private final LocaleService localeService;

    public ResortAccessTypeController(ResortAccessTypeService resortAccessTypeService,
                                      LocaleService localeService) {
        this.resortAccessTypeService = resortAccessTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortAccessTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortAccessTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortAccessTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortAccessTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ResortAccessTypeFilterRequest request) {
        return ResponseEntity.ok(resortAccessTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortAccessTypeRequest request) {
        ResortAccessTypeEntity entity = resortAccessTypeService.getEntityById(id);
        return ResponseEntity.ok(resortAccessTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(resortAccessTypeService.delete(id));
    }
}
