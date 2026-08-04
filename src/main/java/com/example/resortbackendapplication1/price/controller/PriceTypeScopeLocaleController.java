package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.CreatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.UpdatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeLocaleService;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-type-scopes/{price-type-scope-id}/locales")
public class PriceTypeScopeLocaleController {

    private final PriceTypeScopeService priceTypeScopeService;
    private final PriceTypeScopeLocaleService priceTypeScopeLocaleService;
    private final LocaleService localeService;

    public PriceTypeScopeLocaleController(PriceTypeScopeService priceTypeScopeService,
                                          PriceTypeScopeLocaleService priceTypeScopeLocaleService,
                                          LocaleService localeService) {
        this.priceTypeScopeService = priceTypeScopeService;
        this.priceTypeScopeLocaleService = priceTypeScopeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        priceTypeScopeService.getEntityById(priceTypeScopeId);
        return ResponseEntity.ok(priceTypeScopeLocaleService.getAll(priceTypeScopeId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @Valid @RequestBody CreatePriceTypeScopeLocaleRequest request) {
        PriceTypeScopeEntity priceTypeScopeEntity = priceTypeScopeService.getEntityById(priceTypeScopeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceTypeScopeLocaleService.create(request, priceTypeScopeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceTypeScopeLocaleRequest request) {
        PriceTypeScopeLocaleEntity entity = priceTypeScopeLocaleService.getEntityById(priceTypeScopeId, id);
        return ResponseEntity.ok(priceTypeScopeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("price-type-scope-id") Long priceTypeScopeId,
            @PathVariable Long id) {
        PriceTypeScopeLocaleEntity entity = priceTypeScopeLocaleService.getEntityById(priceTypeScopeId, id);
        return ResponseEntity.ok(priceTypeScopeLocaleService.delete(entity));
    }
}
