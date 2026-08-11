package com.example.resortbackendapplication1.price.controller;

import com.example.resortbackendapplication1.price.dto.request.priceunit.CreatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.UpdatePriceUnitRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.price.service.PriceScopeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/price-units")
public class PriceUnitController {

    private final PriceUnitService priceUnitService;
    private final PriceScopeService priceScopeService;
    private final LocaleService localeService;

    public PriceUnitController(PriceUnitService priceUnitService,
                               PriceScopeService priceScopeService,
                               LocaleService localeService) {
        this.priceUnitService = priceUnitService;
        this.priceScopeService = priceScopeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePriceUnitRequest request) {
        List<PriceScopeEntity> priceScopeEntities = priceScopeService.getAll(request.getPriceScopeIds());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(priceUnitService.create(request, priceScopeEntities, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(priceUnitService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PriceUnitFilterRequest request) {
        return ResponseEntity.ok(priceUnitService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceUnitRequest request) {
        PriceUnitEntity entity = priceUnitService.getEntityById(id);
        return ResponseEntity.ok(priceUnitService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        PriceUnitEntity entity = priceUnitService.getEntityById(id);
        return ResponseEntity.ok(priceUnitService.delete(entity));
    }
}
