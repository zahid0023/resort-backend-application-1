package com.example.resortbackendapplication1.resortbasicinfo.controller;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/basic-info")
public class ResortBasicInfoController {

    private final ResortBasicInfoService resortBasicInfoService;
    private final ResortService resortService;
    private final CountryService countryService;
    private final CityService cityService;
    private final LocaleService localeService;

    public ResortBasicInfoController(ResortBasicInfoService resortBasicInfoService,
                                     ResortService resortService,
                                     CountryService countryService,
                                     CityService cityService,
                                     LocaleService localeService) {
        this.resortBasicInfoService = resortBasicInfoService;
        this.resortService = resortService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortBasicInfoRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        CountryEntity countryEntity = countryService.getEntityById(request.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getCityId());
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortBasicInfoLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortBasicInfoService.create(request, resortEntity, countryEntity, cityEntity, localeEntityMap));
    }

    @GetMapping
    public ResponseEntity<?> get(@PathVariable("resort-id") Long resortId) {
        return ResponseEntity.ok(resortBasicInfoService.getByResortId(resortId));
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody UpdateResortBasicInfoRequest request) {
        ResortBasicInfoEntity entity = resortBasicInfoService.getEntityByResortId(resortId);
        CountryEntity countryEntity = countryService.getEntityById(request.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getCityId());
        return ResponseEntity.ok(resortBasicInfoService.update(entity, request, countryEntity, cityEntity));
    }
}
