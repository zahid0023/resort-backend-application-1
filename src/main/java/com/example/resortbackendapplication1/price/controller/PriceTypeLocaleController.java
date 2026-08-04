package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeLocaleService;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-types/{price-type-id}/locales")
public class PriceTypeLocaleController {

    private final PriceTypeService priceTypeService;
    private final PriceTypeLocaleService priceTypeLocaleService;
    private final LocaleService localeService;

    public PriceTypeLocaleController(PriceTypeService priceTypeService,
                                     PriceTypeLocaleService priceTypeLocaleService,
                                     LocaleService localeService) {
        this.priceTypeService = priceTypeService;
        this.priceTypeLocaleService = priceTypeLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("price-type-id") Long priceTypeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        priceTypeService.getEntityById(priceTypeId);
        return ResponseEntity.ok(priceTypeLocaleService.getAll(priceTypeId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("price-type-id") Long priceTypeId,
            @Valid @RequestBody CreatePriceTypeLocaleRequest request) {
        PriceTypeEntity priceTypeEntity = priceTypeService.getEntityById(priceTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceTypeLocaleService.create(request, priceTypeEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("price-type-id") Long priceTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceTypeLocaleRequest request) {
        PriceTypeLocaleEntity entity = priceTypeLocaleService.getEntityById(priceTypeId, id);
        return ResponseEntity.ok(priceTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("price-type-id") Long priceTypeId,
            @PathVariable Long id) {
        PriceTypeLocaleEntity entity = priceTypeLocaleService.getEntityById(priceTypeId, id);
        return ResponseEntity.ok(priceTypeLocaleService.delete(entity));
    }
}
