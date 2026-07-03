package com.example.resortbackendapplication1.pagetype.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.CreatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.PageTypeFilterRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.UpdatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.service.PageTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/page-types")
public class PageTypeController {

    private final PageTypeService pageTypeService;
    private final LocaleService localeService;

    public PageTypeController(PageTypeService pageTypeService,
                              LocaleService localeService) {
        this.pageTypeService = pageTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePageTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreatePageTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(pageTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pageTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PageTypeFilterRequest request) {
        return ResponseEntity.ok(pageTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePageTypeRequest request) {
        PageTypeEntity entity = pageTypeService.getEntityById(id);
        return ResponseEntity.ok(pageTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(pageTypeService.delete(id));
    }
}
