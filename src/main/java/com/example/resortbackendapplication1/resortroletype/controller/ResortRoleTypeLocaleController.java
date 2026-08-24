package com.example.resortbackendapplication1.resortroletype.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.CreateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.UpdateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import com.example.resortbackendapplication1.resortroletype.service.ResortRoleTypeLocaleService;
import com.example.resortbackendapplication1.resortroletype.service.ResortRoleTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resort-role-types/{resort-role-type-id}/locales")
public class ResortRoleTypeLocaleController {

    private final ResortRoleTypeService resortRoleTypeService;
    private final ResortRoleTypeLocaleService resortRoleTypeLocaleService;
    private final LocaleService localeService;

    public ResortRoleTypeLocaleController(ResortRoleTypeService resortRoleTypeService,
                                          ResortRoleTypeLocaleService resortRoleTypeLocaleService,
                                          LocaleService localeService) {
        this.resortRoleTypeService = resortRoleTypeService;
        this.resortRoleTypeLocaleService = resortRoleTypeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-role-type-id") Long resortRoleTypeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        resortRoleTypeService.getEntityById(resortRoleTypeId);
        return ResponseEntity.ok(resortRoleTypeLocaleService.getAll(resortRoleTypeId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(@PathVariable("resort-role-type-id") Long resortRoleTypeId) {
        resortRoleTypeService.getEntityById(resortRoleTypeId);
        return ResponseEntity.ok(resortRoleTypeLocaleService.getActiveCount(resortRoleTypeId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-role-type-id") Long resortRoleTypeId,
            @Valid @RequestBody CreateResortRoleTypeLocaleRequest request) {
        ResortRoleTypeEntity resortRoleTypeEntity = resortRoleTypeService.getEntityById(resortRoleTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoleTypeLocaleService.create(request, resortRoleTypeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-role-type-id") Long resortRoleTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoleTypeLocaleRequest request) {
        ResortRoleTypeLocaleEntity entity = resortRoleTypeLocaleService.getEntityById(resortRoleTypeId, id);
        return ResponseEntity.ok(resortRoleTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-role-type-id") Long resortRoleTypeId,
            @PathVariable Long id) {
        ResortRoleTypeLocaleEntity entity = resortRoleTypeLocaleService.getEntityById(resortRoleTypeId, id);
        return ResponseEntity.ok(resortRoleTypeLocaleService.delete(entity));
    }
}
