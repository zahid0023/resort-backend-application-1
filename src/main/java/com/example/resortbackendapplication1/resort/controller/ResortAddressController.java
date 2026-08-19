package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.UpdateResortAddressRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.service.ResortAddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/address")
public class ResortAddressController {

    private final ResortAddressService resortAddressService;
    private final CountryService countryService;
    private final CityService cityService;

    public ResortAddressController(ResortAddressService resortAddressService,
                                   CountryService countryService,
                                   CityService cityService) {
        this.resortAddressService = resortAddressService;
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<?> getByResortId(@PathVariable("resort-id") Long resortId) {
        return ResponseEntity.ok(resortAddressService.getByResortId(resortId));
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody UpdateResortAddressRequest request) {
        ResortAddressEntity entity = resortAddressService.getEntityByResortId(resortId);
        CountryEntity countryEntity = countryService.getEntityById(request.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getCityId());
        return ResponseEntity.ok(resortAddressService.update(entity, request, countryEntity, cityEntity));
    }
}
