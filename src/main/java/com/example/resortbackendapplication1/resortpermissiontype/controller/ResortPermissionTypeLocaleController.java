package com.example.resortbackendapplication1.resortpermissiontype.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeLocaleService;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resort-permission-types/{resort-permission-type-id}/locales")
public class ResortPermissionTypeLocaleController {

    private final ResortPermissionTypeService resortPermissionTypeService;
    private final ResortPermissionTypeLocaleService resortPermissionTypeLocaleService;
    private final LocaleService localeService;

    public ResortPermissionTypeLocaleController(ResortPermissionTypeService resortPermissionTypeService,
                                                ResortPermissionTypeLocaleService resortPermissionTypeLocaleService,
                                                LocaleService localeService) {
        this.resortPermissionTypeService = resortPermissionTypeService;
        this.resortPermissionTypeLocaleService = resortPermissionTypeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        resortPermissionTypeService.getEntityById(resortPermissionTypeId);
        return ResponseEntity.ok(resortPermissionTypeLocaleService.getAll(resortPermissionTypeId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(@PathVariable("resort-permission-type-id") Long resortPermissionTypeId) {
        resortPermissionTypeService.getEntityById(resortPermissionTypeId);
        return ResponseEntity.ok(resortPermissionTypeLocaleService.getActiveCount(resortPermissionTypeId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @Valid @RequestBody CreateResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeEntity resortPermissionTypeEntity = resortPermissionTypeService.getEntityById(resortPermissionTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortPermissionTypeLocaleService.create(request, resortPermissionTypeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeLocaleEntity entity = resortPermissionTypeLocaleService.getEntityById(resortPermissionTypeId, id);
        return ResponseEntity.ok(resortPermissionTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @PathVariable Long id) {
        ResortPermissionTypeLocaleEntity entity = resortPermissionTypeLocaleService.getEntityById(resortPermissionTypeId, id);
        return ResponseEntity.ok(resortPermissionTypeLocaleService.delete(entity));
    }
}
