package com.example.resortbackendapplication1.resortpermissiontype.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resort-permission-types")
public class ResortPermissionTypeController {

    private final ResortPermissionTypeService resortPermissionTypeService;
    private final LocaleService localeService;

    public ResortPermissionTypeController(ResortPermissionTypeService resortPermissionTypeService,
                                          LocaleService localeService) {
        this.resortPermissionTypeService = resortPermissionTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortPermissionTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortPermissionTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortPermissionTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortPermissionTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ResortPermissionTypeFilterRequest request) {
        return ResponseEntity.ok(resortPermissionTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortPermissionTypeRequest request) {
        ResortPermissionTypeEntity entity = resortPermissionTypeService.getEntityById(id);
        return ResponseEntity.ok(resortPermissionTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(resortPermissionTypeService.delete(id));
    }
}
