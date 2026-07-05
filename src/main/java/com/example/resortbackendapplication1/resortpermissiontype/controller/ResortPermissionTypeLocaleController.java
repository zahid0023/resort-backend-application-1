package com.example.resortbackendapplication1.resortpermissiontype.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeLocaleService;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @Valid @RequestBody CreateResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeEntity resortPermissionType = resortPermissionTypeService.getEntityById(resortPermissionTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortPermissionTypeLocaleService.create(resortPermissionType, localeEntity, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeLocaleEntity entity = resortPermissionTypeLocaleService.getEntityById(resortPermissionTypeId, id);
        return ResponseEntity.ok(resortPermissionTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(
            @PathVariable("resort-permission-type-id") Long resortPermissionTypeId,
            @PathVariable Long id) {
        ResortPermissionTypeLocaleEntity entity = resortPermissionTypeLocaleService.getEntityById(resortPermissionTypeId, id);
        return ResponseEntity.ok(resortPermissionTypeLocaleService.delete(entity));
    }
}
