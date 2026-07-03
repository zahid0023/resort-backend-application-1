package com.example.resortbackendapplication1.uiblocksection.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.CreateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UiBlockSectionFilterRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UpdateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.service.UiBlockSectionService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ui-block-sections")
public class UiBlockSectionController {

    private final UiBlockSectionService uiBlockSectionService;
    private final LocaleService localeService;

    public UiBlockSectionController(UiBlockSectionService uiBlockSectionService,
                                    LocaleService localeService) {
        this.uiBlockSectionService = uiBlockSectionService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUiBlockSectionRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateUiBlockSectionLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(uiBlockSectionService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(uiBlockSectionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject UiBlockSectionFilterRequest request) {
        return ResponseEntity.ok(uiBlockSectionService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUiBlockSectionRequest request) {
        UiBlockSectionEntity entity = uiBlockSectionService.getEntityById(id);
        return ResponseEntity.ok(uiBlockSectionService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(uiBlockSectionService.delete(id));
    }
}
