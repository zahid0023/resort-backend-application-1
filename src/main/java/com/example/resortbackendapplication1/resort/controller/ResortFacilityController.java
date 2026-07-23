package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.SetResortFacilityHighlightsRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import com.example.resortbackendapplication1.resort.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities")
public class ResortFacilityController {

    private final ResortService resortService;
    private final ResortFacilityGroupService resortFacilityGroupService;
    private final ResortFacilityService resortFacilityService;
    private final FacilityService facilityService;
    private final FacilityPriceTypeService facilityPriceTypeService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;
    private final LocaleService localeService;

    public ResortFacilityController(ResortService resortService,
                                    ResortFacilityGroupService resortFacilityGroupService,
                                    ResortFacilityService resortFacilityService,
                                    FacilityService facilityService,
                                    FacilityPriceTypeService facilityPriceTypeService,
                                    PriceUnitService priceUnitService,
                                    CurrencyService currencyService,
                                    LocaleService localeService) {
        this.resortService = resortService;
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.resortFacilityService = resortFacilityService;
        this.facilityService = facilityService;
        this.facilityPriceTypeService = facilityPriceTypeService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortFacilityRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getEntityById(request.getResortFacilityGroupId(), resortId);
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        FacilityPriceTypeEntity facilityPriceTypeEntity = request.getFacilityPriceTypeId() != null
                ? facilityPriceTypeService.getEntityById(request.getFacilityPriceTypeId())
                : null;
        PriceUnitEntity priceUnitEntity = request.getResortFacilityPrice() != null
                ? priceUnitService.getEntityById(request.getResortFacilityPrice().getPriceUnitId())
                : null;
        CurrencyEntity currencyEntity = request.getResortFacilityPrice() != null
                ? currencyService.getEntityById(request.getResortFacilityPrice().getCurrencyId())
                : null;
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortFacilityLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.create(request, resortEntity, resortFacilityGroupEntity, facilityEntity, facilityPriceTypeEntity, priceUnitEntity, currencyEntity, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.getById(id, resortId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortFacilityFilterRequest request,
            @RequestParam(required = false) Long resortFacilityGroupId) {
        return ResponseEntity.ok(resortFacilityService.getAll(request, resortFacilityGroupId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,   
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityRequest request) {
        ResortFacilityEntity entity = resortFacilityService.getEntityById(id);
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        FacilityPriceTypeEntity facilityPriceTypeEntity = request.getFacilityPriceTypeId() != null
                ? facilityPriceTypeService.getEntityById(request.getFacilityPriceTypeId())
                : null;
        return ResponseEntity.ok(resortFacilityService.update(entity, request, facilityEntity, facilityPriceTypeEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.delete(id));
    }

    @PutMapping("/highlights")
    public ResponseEntity<?> setHighlights(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody SetResortFacilityHighlightsRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortFacilityService.setHighlights(resortId, request));
    }
}
