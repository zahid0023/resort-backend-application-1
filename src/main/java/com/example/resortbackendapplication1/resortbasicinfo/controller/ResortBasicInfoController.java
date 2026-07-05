package com.example.resortbackendapplication1.resortbasicinfo.controller;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
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
@RequestMapping("/api/v1/resort-basic-info")
public class ResortBasicInfoController {

    private final ResortBasicInfoService resortBasicInfoService;
    private final CountryService countryService;
    private final CityService cityService;
    private final LocaleService localeService;

    public ResortBasicInfoController(ResortBasicInfoService resortBasicInfoService,
                                     CountryService countryService,
                                     CityService cityService,
                                     LocaleService localeService) {
        this.resortBasicInfoService = resortBasicInfoService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortBasicInfoRequest request) {
        CountryEntity countryEntity = countryService.getEntityById(request.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getCityId());
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortBasicInfoLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortBasicInfoService.create(request, countryEntity, cityEntity, localeEntityMap));
    }

    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(resortBasicInfoService.get());
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody UpdateResortBasicInfoRequest request) {
        ResortBasicInfoEntity entity = resortBasicInfoService.getEntity();
        CountryEntity countryEntity = countryService.getEntityById(request.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getCityId());
        return ResponseEntity.ok(resortBasicInfoService.update(entity, request, countryEntity, cityEntity));
    }
}
