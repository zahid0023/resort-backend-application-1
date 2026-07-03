package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitLocaleService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-units/{price-unit-id}/locales")
public class PriceUnitLocaleController {

    private final PriceUnitService priceUnitService;
    private final PriceUnitLocaleService priceUnitLocaleService;
    private final LocaleService localeService;

    public PriceUnitLocaleController(PriceUnitService priceUnitService,
                                     PriceUnitLocaleService priceUnitLocaleService,
                                     LocaleService localeService) {
        this.priceUnitService = priceUnitService;
        this.priceUnitLocaleService = priceUnitLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("price-unit-id") Long priceUnitId,
            @Valid @RequestBody CreatePriceUnitLocaleRequest request) {
        PriceUnitEntity priceUnit = priceUnitService.getEntityById(priceUnitId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceUnitLocaleService.create(priceUnit, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("price-unit-id") Long priceUnitId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceUnitLocaleRequest request) {
        PriceUnitLocaleEntity entity = priceUnitLocaleService.getEntityById(priceUnitId, id);
        return ResponseEntity.ok(priceUnitLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("price-unit-id") Long priceUnitId,
            @PathVariable Long id) {
        PriceUnitLocaleEntity entity = priceUnitLocaleService.getEntityById(priceUnitId, id);
        return ResponseEntity.ok(priceUnitLocaleService.delete(entity));
    }
}
