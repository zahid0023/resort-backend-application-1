package com.example.resortbackendapplication1.uiblocksection.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.UpdateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionLocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.service.UiBlockSectionLocaleService;
import com.example.resortbackendapplication1.uiblocksection.service.UiBlockSectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ui-block-sections/{ui-block-section-id}/locales")
public class UiBlockSectionLocaleController {

    private final UiBlockSectionService uiBlockSectionService;
    private final UiBlockSectionLocaleService uiBlockSectionLocaleService;
    private final LocaleService localeService;

    public UiBlockSectionLocaleController(UiBlockSectionService uiBlockSectionService,
                                          UiBlockSectionLocaleService uiBlockSectionLocaleService,
                                          LocaleService localeService) {
        this.uiBlockSectionService = uiBlockSectionService;
        this.uiBlockSectionLocaleService = uiBlockSectionLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("ui-block-section-id") Long uiBlockSectionId,
            @Valid @RequestBody CreateUiBlockSectionLocaleRequest request) {
        UiBlockSectionEntity uiBlockSection = uiBlockSectionService.getEntityById(uiBlockSectionId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiBlockSectionLocaleService.create(uiBlockSection, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("ui-block-section-id") Long uiBlockSectionId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateUiBlockSectionLocaleRequest request) {
        UiBlockSectionLocaleEntity entity = uiBlockSectionLocaleService.getEntityById(uiBlockSectionId, id);
        return ResponseEntity.ok(uiBlockSectionLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("ui-block-section-id") Long uiBlockSectionId,
            @PathVariable Long id) {
        UiBlockSectionLocaleEntity entity = uiBlockSectionLocaleService.getEntityById(uiBlockSectionId, id);
        return ResponseEntity.ok(uiBlockSectionLocaleService.delete(entity));
    }
}
