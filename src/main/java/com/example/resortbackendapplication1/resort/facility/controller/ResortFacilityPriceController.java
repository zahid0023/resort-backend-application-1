package com.example.resortbackendapplication1.resort.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.price.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityPriceService;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityService;
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
    private final FacilityPriceTypeService facilityPriceTypeService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortFacilityPriceController(ResortFacilityPriceService resortFacilityPriceService,
                                         ResortFacilityService resortFacilityService,
                                         FacilityPriceTypeService facilityPriceTypeService,
                                         PriceUnitService priceUnitService,
                                         CurrencyService currencyService) {
        this.resortFacilityPriceService = resortFacilityPriceService;
        this.resortFacilityService = resortFacilityService;
        this.facilityPriceTypeService = facilityPriceTypeService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @Valid @RequestBody CreateResortFacilityPriceRequest request) {
        ResortFacilityEntity resortFacilityEntity = resortFacilityService.getEntityById(resortId, resortFacilityId);
        FacilityPriceTypeEntity facilityPriceTypeEntity = facilityPriceTypeService.getEntityById(request.getPriceTypeId());
        PriceUnitEntity priceUnitEntity = request.getPriceUnitId() != null
                ? priceUnitService.getEntityById(request.getPriceUnitId())
                : null;
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortFacilityPriceService.create(
                request, resortFacilityEntity, facilityPriceTypeEntity, priceUnitEntity, currencyEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id) {
        resortFacilityService.getEntityById(resortId, resortFacilityId);
        return ResponseEntity.ok(resortFacilityPriceService.getById(resortFacilityId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        resortFacilityService.getEntityById(resortId, resortFacilityId);
        return ResponseEntity.ok(resortFacilityPriceService.getAll(resortFacilityId, paginatedRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityPriceRequest request) {
        resortFacilityService.getEntityById(resortId, resortFacilityId);
        ResortFacilityPriceEntity entity = resortFacilityPriceService.getEntityById(resortFacilityId, id);
        return ResponseEntity.ok(resortFacilityPriceService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @PathVariable Long id) {
        resortFacilityService.getEntityById(resortId, resortFacilityId);
        ResortFacilityPriceEntity entity = resortFacilityPriceService.getEntityById(resortFacilityId, id);
        return ResponseEntity.ok(resortFacilityPriceService.delete(entity));
    }
}
