package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities")
public class ResortFacilityController {

    private final ResortFacilityService resortFacilityService;
    private final ResortService resortService;
    private final ResortFacilityGroupService resortFacilityGroupService;
    private final FacilityService facilityService;
    private final LocaleService localeService;
    private final DayOfWeekService dayOfWeekService;
    private final PriceTypeService priceTypeService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortFacilityController(ResortFacilityService resortFacilityService,
                                    ResortService resortService,
                                    ResortFacilityGroupService resortFacilityGroupService,
                                    FacilityService facilityService,
                                    LocaleService localeService,
                                    DayOfWeekService dayOfWeekService,
                                    PriceTypeService priceTypeService,
                                    PriceUnitService priceUnitService,
                                    CurrencyService currencyService) {
        this.resortFacilityService = resortFacilityService;
        this.resortService = resortService;
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.facilityService = facilityService;
        this.localeService = localeService;
        this.dayOfWeekService = dayOfWeekService;
        this.priceTypeService = priceTypeService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortFacilityRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        ResortFacilityGroupEntity resortFacilityGroupEntity =
                resortFacilityGroupService.getEntityById(resortId, request.getResortFacilityGroupId());
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        List<DayOfWeekEntity> allDaysOfWeek = request.getOperatingHours() != null && !request.getOperatingHours().isEmpty()
                ? dayOfWeekService.getAllActiveEntities()
                : List.of();
        CreateResortFacilityPriceRequest priceRequest = request.getPrice();
        PriceTypeEntity priceTypeEntity = priceRequest != null
                ? priceTypeService.getEntityById(priceRequest.getPriceTypeId())
                : null;
        PriceUnitEntity priceUnitEntity = priceRequest != null && priceRequest.getPriceUnitId() != null
                ? priceUnitService.getEntityById(priceRequest.getPriceUnitId())
                : null;
        CurrencyEntity currencyEntity = priceRequest != null
                ? currencyService.getEntityById(priceRequest.getCurrencyId())
                : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.create(request, resortEntity, resortFacilityGroupEntity, facilityEntity,
                        localeEntity, allDaysOfWeek, priceTypeEntity, priceUnitEntity, currencyEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortFacilityFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortFacilityService.getAll(resortId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityRequest request) {
        ResortFacilityEntity entity = resortFacilityService.getEntityById(resortId, id);
        ResortFacilityGroupEntity resortFacilityGroupEntity =
                resortFacilityGroupService.getEntityById(resortId, request.getResortFacilityGroupId());
        return ResponseEntity.ok(resortFacilityService.update(entity, resortFacilityGroupEntity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortFacilityEntity entity = resortFacilityService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortFacilityService.delete(entity));
    }
}
