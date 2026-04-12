package com.example.resortapplication1.controller;

import com.example.resortapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortapplication1.dto.request.templatepageslots.CreateTemplatePageSlotRequest;
import com.example.resortapplication1.dto.request.templatepageslots.UpdateTemplatePageSlotRequest;
import com.example.resortapplication1.model.entity.TemplatePageEntity;
import com.example.resortapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortapplication1.model.entity.UiBlockCategoryEntity;
import com.example.resortapplication1.service.TemplatePageService;
import com.example.resortapplication1.service.TemplatePageSlotService;
import com.example.resortapplication1.service.UiBlockCategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots")
public class TemplatePageSlotController {

    private final TemplatePageSlotService templatePageSlotService;
    private final TemplatePageService templatePageService;
    private final UiBlockCategoryService uiBlockCategoryService;

    public TemplatePageSlotController(TemplatePageSlotService templatePageSlotService,
                                      TemplatePageService templatePageService,
                                      UiBlockCategoryService uiBlockCategoryService) {
        this.templatePageSlotService = templatePageSlotService;
        this.templatePageService = templatePageService;
        this.uiBlockCategoryService = uiBlockCategoryService;
    }

    @PostMapping
    public ResponseEntity<?> createTemplatePageSlot(@PathVariable("template-id") Long templateId, @PathVariable("template-page-id") Long templatePageId, @RequestBody CreateTemplatePageSlotRequest request) {
        TemplatePageEntity templatePageEntity = templatePageService.getTemplatePageEntity(templateId, templatePageId);
        UiBlockCategoryEntity uiBlockCategoryEntity = uiBlockCategoryService.getUiBlockCategoryById(request.getUiBlockCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(templatePageSlotService.createTemplatePageSlot(request, templatePageEntity, uiBlockCategoryEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplatePageSlot(@PathVariable("template-id") Long templateId, @PathVariable("template-page-id") Long templatePageId, @PathVariable Long id) {
        return ResponseEntity.ok(templatePageSlotService.getTemplatePageSlot(templateId, templatePageId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllTemplatePageSlots(@PathVariable("template-id") Long templateId, @PathVariable("template-page-id") Long templatePageId, @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "slotName", "slotOrder"));
        return ResponseEntity.ok(templatePageSlotService.getAllTemplatePageSlots(templateId, templatePageId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplatePageSlot(@PathVariable("template-id") Long templateId, @PathVariable("template-page-id") Long templatePageId, @PathVariable Long id, @RequestBody UpdateTemplatePageSlotRequest request) {
        TemplatePageSlotEntity templatePageSlotEntity = templatePageSlotService.getTemplatePageSlotEntity(templateId, templatePageId, id);
        UiBlockCategoryEntity uiBlockCategoryEntity = uiBlockCategoryService.getUiBlockCategoryById(request.getUiBlockCategoryId());
        return ResponseEntity.ok(templatePageSlotService.updateTemplatePageSlot(request, templatePageSlotEntity, uiBlockCategoryEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplatePageSlot(@PathVariable("template-id") Long templateId, @PathVariable("template-page-id") Long templatePageId, @PathVariable Long id) {
        return ResponseEntity.ok(templatePageSlotService.deleteTemplatePageSlot(templateId, templatePageId, id));
    }
}
