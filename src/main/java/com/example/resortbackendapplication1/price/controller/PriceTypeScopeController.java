package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.price.dto.request.pricetypescope.CreatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.PriceTypeScopeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.UpdatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-type-scopes")
public class PriceTypeScopeController {

    private final PriceTypeScopeService priceTypeScopeService;
    private final LocaleService localeService;

    public PriceTypeScopeController(PriceTypeScopeService priceTypeScopeService,
                                    LocaleService localeService) {
        this.priceTypeScopeService = priceTypeScopeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePriceTypeScopeRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(priceTypeScopeService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(priceTypeScopeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PriceTypeScopeFilterRequest request) {
        return ResponseEntity.ok(priceTypeScopeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceTypeScopeRequest request) {
        PriceTypeScopeEntity entity = priceTypeScopeService.getEntityById(id);
        return ResponseEntity.ok(priceTypeScopeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        PriceTypeScopeEntity entity = priceTypeScopeService.getEntityById(id);
        return ResponseEntity.ok(priceTypeScopeService.delete(entity));
    }
}
