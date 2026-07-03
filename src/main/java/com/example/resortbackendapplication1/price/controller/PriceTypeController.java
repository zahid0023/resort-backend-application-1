package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/price-types")
public class PriceTypeController {

    private final PriceTypeService priceTypeService;
    private final LocaleService localeService;

    public PriceTypeController(PriceTypeService priceTypeService,
                               LocaleService localeService) {
        this.priceTypeService = priceTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePriceTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreatePriceTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(priceTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(priceTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PriceTypeFilterRequest request) {
        return ResponseEntity.ok(priceTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceTypeRequest request) {
        PriceTypeEntity entity = priceTypeService.getEntityById(id);
        return ResponseEntity.ok(priceTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(priceTypeService.delete(id));
    }
}
