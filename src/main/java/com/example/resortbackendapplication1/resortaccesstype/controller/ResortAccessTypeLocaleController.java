package com.example.resortbackendapplication1.resortaccesstype.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.CreateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.UpdateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeLocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeLocaleService;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resort-access-types/{resort-access-type-id}/locales")
public class ResortAccessTypeLocaleController {

    private final ResortAccessTypeService resortAccessTypeService;
    private final ResortAccessTypeLocaleService resortAccessTypeLocaleService;
    private final LocaleService localeService;

    public ResortAccessTypeLocaleController(ResortAccessTypeService resortAccessTypeService,
                                            ResortAccessTypeLocaleService resortAccessTypeLocaleService,
                                            LocaleService localeService) {
        this.resortAccessTypeService = resortAccessTypeService;
        this.resortAccessTypeLocaleService = resortAccessTypeLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @PathVariable("resort-access-type-id") Long resortAccessTypeId,
            @Valid @RequestBody CreateResortAccessTypeLocaleRequest request) {
        ResortAccessTypeEntity resortAccessType = resortAccessTypeService.getEntityById(resortAccessTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortAccessTypeLocaleService.create(resortAccessType, localeEntity, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable("resort-access-type-id") Long resortAccessTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortAccessTypeLocaleRequest request) {
        ResortAccessTypeLocaleEntity entity = resortAccessTypeLocaleService.getEntityById(resortAccessTypeId, id);
        return ResponseEntity.ok(resortAccessTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(
            @PathVariable("resort-access-type-id") Long resortAccessTypeId,
            @PathVariable Long id) {
        ResortAccessTypeLocaleEntity entity = resortAccessTypeLocaleService.getEntityById(resortAccessTypeId, id);
        return ResponseEntity.ok(resortAccessTypeLocaleService.delete(entity));
    }
}
