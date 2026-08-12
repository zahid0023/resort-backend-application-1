package com.example.resortbackendapplication1.resortroletype.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.CreateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.ResortRoleTypeFilterRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.UpdateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.service.ResortRoleTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resort-role-types")
public class ResortRoleTypeController {

    private final ResortRoleTypeService resortRoleTypeService;
    private final LocaleService localeService;

    public ResortRoleTypeController(ResortRoleTypeService resortRoleTypeService,
                                    LocaleService localeService) {
        this.resortRoleTypeService = resortRoleTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortRoleTypeRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoleTypeService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortRoleTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ResortRoleTypeFilterRequest request) {
        return ResponseEntity.ok(resortRoleTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoleTypeRequest request) {
        ResortRoleTypeEntity entity = resortRoleTypeService.getEntityById(id);
        return ResponseEntity.ok(resortRoleTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ResortRoleTypeEntity entity = resortRoleTypeService.getEntityById(id);
        return ResponseEntity.ok(resortRoleTypeService.delete(entity));
    }
}
