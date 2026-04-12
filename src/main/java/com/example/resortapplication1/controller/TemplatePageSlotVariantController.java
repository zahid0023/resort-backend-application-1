package com.example.resortapplication1.controller;

import com.example.resortapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortapplication1.dto.request.templatepageslotvariants.CreateTemplatePageSlotVariantRequest;
import com.example.resortapplication1.dto.request.templatepageslotvariants.UpdateTemplatePageSlotVariantRequest;
import com.example.resortapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortapplication1.model.entity.TemplatePageSlotVariantEntity;
import com.example.resortapplication1.model.entity.UiBlockDefinitionEntity;
import com.example.resortapplication1.service.TemplatePageSlotService;
import com.example.resortapplication1.service.TemplatePageSlotVariantService;
import com.example.resortapplication1.service.UiBlockDefinitionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants")
public class TemplatePageSlotVariantController {

    private final TemplatePageSlotVariantService templatePageSlotVariantService;
    private final TemplatePageSlotService templatePageSlotService;
    private final UiBlockDefinitionService uiBlockDefinitionService;

    public TemplatePageSlotVariantController(TemplatePageSlotVariantService templatePageSlotVariantService,
                                             TemplatePageSlotService templatePageSlotService,
                                             UiBlockDefinitionService uiBlockDefinitionService) {
        this.templatePageSlotVariantService = templatePageSlotVariantService;
        this.templatePageSlotService = templatePageSlotService;
        this.uiBlockDefinitionService = uiBlockDefinitionService;
    }

    @PostMapping
    public ResponseEntity<?> createTemplatePageSlotVariant(
            @PathVariable("template-id") Long templateId,
            @PathVariable("template-page-id") Long templatePageId,
            @PathVariable("template-page-slot-id") Long templatePageSlotId,
            @RequestBody CreateTemplatePageSlotVariantRequest request) {
        TemplatePageSlotEntity templatePageSlotEntity = templatePageSlotService.getTemplatePageSlotEntity(templateId, templatePageId, templatePageSlotId);
        UiBlockDefinitionEntity uiBlockDefinitionEntity = uiBlockDefinitionService.getUiBlockDefinitionById(request.getUiBlockDefinitionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                templatePageSlotVariantService.createTemplatePageSlotVariant(request, templatePageSlotEntity, uiBlockDefinitionEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplatePageSlotVariant(
            @PathVariable("template-id") Long templateId,
            @PathVariable("template-page-id") Long templatePageId,
            @PathVariable("template-page-slot-id") Long templatePageSlotId,
            @PathVariable Long id) {
        return ResponseEntity.ok(templatePageSlotVariantService.getTemplatePageSlotVariant(templateId, templatePageId, templatePageSlotId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllTemplatePageSlotVariants(
            @PathVariable("template-id") Long templateId,
            @PathVariable("template-page-id") Long templatePageId,
            @PathVariable("template-page-slot-id") Long templatePageSlotId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "displayOrder"));
        return ResponseEntity.ok(templatePageSlotVariantService.getAllTemplatePageSlotVariants(templateId, templatePageId, templatePageSlotId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplatePageSlotVariant(
            @PathVariable("template-id") Long templateId,
            @PathVariable("template-page-id") Long templatePageId,
            @PathVariable("template-page-slot-id") Long templatePageSlotId,
            @PathVariable Long id,
            @RequestBody UpdateTemplatePageSlotVariantRequest request) {
        TemplatePageSlotVariantEntity templatePageSlotVariantEntity = templatePageSlotVariantService.getTemplatePageSlotVariantEntity(templateId, templatePageId, templatePageSlotId, id);
        UiBlockDefinitionEntity uiBlockDefinitionEntity = request.getUiBlockDefinitionId() != null
                ? uiBlockDefinitionService.getUiBlockDefinitionById(request.getUiBlockDefinitionId())
                : null;
        return ResponseEntity.ok(templatePageSlotVariantService.updateTemplatePageSlotVariant(request, templatePageSlotVariantEntity, uiBlockDefinitionEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplatePageSlotVariant(
            @PathVariable("template-id") Long templateId,
            @PathVariable("template-page-id") Long templatePageId,
            @PathVariable("template-page-slot-id") Long templatePageSlotId,
            @PathVariable Long id) {
        return ResponseEntity.ok(templatePageSlotVariantService.deleteTemplatePageSlotVariant(templateId, templatePageId, templatePageSlotId, id));
    }
}
