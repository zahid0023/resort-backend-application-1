package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.price.dto.request.pricescope.CreatePriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.PriceScopeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.UpdatePriceScopeRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.service.PriceScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-scopes")
public class PriceScopeController {

    private final PriceScopeService priceScopeService;
    private final LocaleService localeService;

    public PriceScopeController(PriceScopeService priceScopeService,
                                LocaleService localeService) {
        this.priceScopeService = priceScopeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePriceScopeRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(priceScopeService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(priceScopeService.getById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount() {
        return ResponseEntity.ok(priceScopeService.getActiveCount());
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PriceScopeFilterRequest request) {
        return ResponseEntity.ok(priceScopeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceScopeRequest request) {
        PriceScopeEntity entity = priceScopeService.getEntityById(id);
        return ResponseEntity.ok(priceScopeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        PriceScopeEntity entity = priceScopeService.getEntityById(id);
        return ResponseEntity.ok(priceScopeService.delete(entity));
    }
}
