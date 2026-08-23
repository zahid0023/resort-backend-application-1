package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityprice.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityPriceService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities/{facility-id}/prices")
public class ResortFacilityPriceController {

    private final ResortFacilityPriceService resortFacilityPriceService;
    private final ResortFacilityService resortFacilityService;
    private final PriceTypeService priceTypeService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortFacilityPriceController(ResortFacilityPriceService resortFacilityPriceService,
                                         ResortFacilityService resortFacilityService,
                                         PriceTypeService priceTypeService,
                                         PriceUnitService priceUnitService,
                                         CurrencyService currencyService) {
        this.resortFacilityPriceService = resortFacilityPriceService;
        this.resortFacilityService = resortFacilityService;
        this.priceTypeService = priceTypeService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody CreateResortFacilityPriceRequest request) {
        ResortFacilityEntity resortFacilityEntity = resortFacilityService.getEntityById(resortId, facilityId);
        PriceTypeEntity priceTypeEntity = priceTypeService.getEntityById(request.getPriceTypeId());
        PriceUnitEntity priceUnitEntity = request.getPriceUnitId() != null
                ? priceUnitService.getEntityById(request.getPriceUnitId())
                : null;
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortFacilityPriceService.create(
                request, resortFacilityEntity, priceTypeEntity, priceUnitEntity, currencyEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-id") Long facilityId,
            @PathVariable Long id) {
        resortFacilityService.getEntityById(resortId, facilityId);
        return ResponseEntity.ok(resortFacilityPriceService.getById(facilityId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-id") Long facilityId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        resortFacilityService.getEntityById(resortId, facilityId);
        return ResponseEntity.ok(resortFacilityPriceService.getAll(facilityId, paginatedRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-id") Long facilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityPriceRequest request) {
        resortFacilityService.getEntityById(resortId, facilityId);
        ResortFacilityPriceEntity entity = resortFacilityPriceService.getEntityById(facilityId, id);
        return ResponseEntity.ok(resortFacilityPriceService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-id") Long facilityId,
            @PathVariable Long id) {
        resortFacilityService.getEntityById(resortId, facilityId);
        ResortFacilityPriceEntity entity = resortFacilityPriceService.getEntityById(facilityId, id);
        return ResponseEntity.ok(resortFacilityPriceService.delete(entity));
    }
}
