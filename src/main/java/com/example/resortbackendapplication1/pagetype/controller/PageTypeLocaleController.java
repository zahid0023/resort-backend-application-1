package com.example.resortbackendapplication1.pagetype.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.UpdatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeLocaleEntity;
import com.example.resortbackendapplication1.pagetype.service.PageTypeLocaleService;
import com.example.resortbackendapplication1.pagetype.service.PageTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/page-types/{page-type-id}/locales")
public class PageTypeLocaleController {

    private final PageTypeService pageTypeService;
    private final PageTypeLocaleService pageTypeLocaleService;
    private final LocaleService localeService;

    public PageTypeLocaleController(PageTypeService pageTypeService,
                                    PageTypeLocaleService pageTypeLocaleService,
                                    LocaleService localeService) {
        this.pageTypeService = pageTypeService;
        this.pageTypeLocaleService = pageTypeLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("page-type-id") Long pageTypeId,
            @Valid @RequestBody CreatePageTypeLocaleRequest request) {
        PageTypeEntity pageType = pageTypeService.getEntityById(pageTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pageTypeLocaleService.create(pageType, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("page-type-id") Long pageTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePageTypeLocaleRequest request) {
        PageTypeLocaleEntity entity = pageTypeLocaleService.getEntityById(pageTypeId, id);
        return ResponseEntity.ok(pageTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("page-type-id") Long pageTypeId,
            @PathVariable Long id) {
        PageTypeLocaleEntity entity = pageTypeLocaleService.getEntityById(pageTypeId, id);
        return ResponseEntity.ok(pageTypeLocaleService.delete(entity));
    }
}
