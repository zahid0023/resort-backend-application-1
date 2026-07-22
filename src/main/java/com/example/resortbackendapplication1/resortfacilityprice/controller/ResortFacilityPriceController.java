package com.example.resortbackendapplication1.resortfacilityprice.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.ResortFacilityPriceFilterRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resortfacilityprice.service.ResortFacilityPriceService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices")
public class ResortFacilityPriceController {

    private final ResortFacilityPriceService resortFacilityPriceService;
    private final ResortFacilityService resortFacilityService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortFacilityPriceController(ResortFacilityPriceService resortFacilityPriceService,
                                         ResortFacilityService resortFacilityService,
                                         PriceUnitService priceUnitService,
                                         CurrencyService currencyService) {
        this.resortFacilityPriceService = resortFacilityPriceService;
        this.resortFacilityService = resortFacilityService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @Valid @RequestBody CreateResortFacilityPriceRequest request) {
        ResortFacilityEntity resortFacility = resortFacilityService.getEntityById(resortFacilityId);
        PriceUnitEntity priceUnit = priceUnitService.getEntityById(request.getPriceUnitId());
        CurrencyEntity currency = currencyService.getEntityById(request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityPriceService.create(request, resortFacility, priceUnit, currency));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id) {
        resortFacilityService.getEntityById(resortFacilityId);
        return ResponseEntity.ok(resortFacilityPriceService.getById(resortFacilityId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @Valid @ParameterObject ResortFacilityPriceFilterRequest request) {
        resortFacilityService.getEntityById(resortFacilityId);
        return ResponseEntity.ok(resortFacilityPriceService.getAll(request, resortFacilityId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityPriceRequest request) {
        resortFacilityService.getEntityById(resortFacilityId);
        ResortFacilityPriceEntity entity = resortFacilityPriceService.getEntityById(resortFacilityId, id);
        PriceUnitEntity priceUnit = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.ok(resortFacilityPriceService.update(entity, request, priceUnit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id) {
        resortFacilityService.getEntityById(resortFacilityId);
        ResortFacilityPriceEntity entity = resortFacilityPriceService.getEntityById(resortFacilityId, id);
        return ResponseEntity.ok(resortFacilityPriceService.delete(entity));
    }
}
