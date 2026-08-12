package com.example.resortbackendapplication1.resortpermissiontype.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(resortPermissionTypeService.create(request, localeEntity));
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
        ResortPermissionTypeEntity entity = resortPermissionTypeService.getEntityById(id);
        return ResponseEntity.ok(resortPermissionTypeService.delete(entity));
    }
}
