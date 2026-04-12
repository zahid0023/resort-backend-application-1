package com.example.resortapplication1.controller;

import com.example.resortapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortapplication1.dto.request.templatepages.CreateTemplatePageRequest;
import com.example.resortapplication1.dto.request.templatepages.UpdateTemplatePageRequest;
import com.example.resortapplication1.model.entity.TemplateEntity;
import com.example.resortapplication1.model.entity.TemplatePageEntity;
import com.example.resortapplication1.service.TemplatePageService;
import com.example.resortapplication1.service.TemplateService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/templates/{template-id}/template-pages")
public class TemplatePageController {

    private final TemplatePageService templatePageService;
    private final TemplateService templateService;

    public TemplatePageController(TemplatePageService templatePageService,
                                  TemplateService templateService) {
        this.templatePageService = templatePageService;
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<?> createTemplatePage(@PathVariable("template-id") Long templateId, @RequestBody CreateTemplatePageRequest request) {
        TemplateEntity templateEntity = templateService.getTemplateById(templateId);
        return ResponseEntity.status(HttpStatus.CREATED).body(templatePageService.createTemplatePage(request, templateEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplatePage(@PathVariable("template-id") Long templateId, @PathVariable Long id) {
        return ResponseEntity.ok(templatePageService.getTemplatePage(templateId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllTemplatePages(@PathVariable("template-id") Long templateId, @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "pageName", "pageSlug", "pageOrder"));
        return ResponseEntity.ok(templatePageService.getAllTemplatePages(templateId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplatePage(@PathVariable("template-id") Long templateId, @PathVariable Long id, @RequestBody UpdateTemplatePageRequest request) {
        TemplatePageEntity templatePageEntity = templatePageService.getTemplatePageEntity(templateId, id);
        return ResponseEntity.ok(templatePageService.updateTemplatePage(request, templatePageEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplatePage(@PathVariable("template-id") Long templateId, @PathVariable Long id) {
        return ResponseEntity.ok(templatePageService.deleteTemplatePage(templateId, id));
    }
}
