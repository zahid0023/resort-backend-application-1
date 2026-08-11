package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.CreatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.UpdatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.price.service.PriceScopeLocaleService;
import com.example.resortbackendapplication1.price.service.PriceScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-scopes/{price-scope-id}/locales")
public class PriceScopeLocaleController {

    private final PriceScopeService priceScopeService;
    private final PriceScopeLocaleService priceScopeLocaleService;
    private final LocaleService localeService;

    public PriceScopeLocaleController(PriceScopeService priceScopeService,
                                      PriceScopeLocaleService priceScopeLocaleService,
                                      LocaleService localeService) {
        this.priceScopeService = priceScopeService;
        this.priceScopeLocaleService = priceScopeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("price-scope-id") Long priceScopeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        priceScopeService.getEntityById(priceScopeId);
        return ResponseEntity.ok(priceScopeLocaleService.getAll(priceScopeId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("price-scope-id") Long priceScopeId) {
        priceScopeService.getEntityById(priceScopeId);
        return ResponseEntity.ok(priceScopeLocaleService.getCount(priceScopeId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("price-scope-id") Long priceScopeId,
            @Valid @RequestBody CreatePriceScopeLocaleRequest request) {
        PriceScopeEntity priceScopeEntity = priceScopeService.getEntityById(priceScopeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceScopeLocaleService.create(request, priceScopeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("price-scope-id") Long priceScopeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceScopeLocaleRequest request) {
        PriceScopeLocaleEntity entity = priceScopeLocaleService.getEntityById(priceScopeId, id);
        return ResponseEntity.ok(priceScopeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("price-scope-id") Long priceScopeId,
            @PathVariable Long id) {
        PriceScopeLocaleEntity entity = priceScopeLocaleService.getEntityById(priceScopeId, id);
        return ResponseEntity.ok(priceScopeLocaleService.delete(entity));
    }
}
