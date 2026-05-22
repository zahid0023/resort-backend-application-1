package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.dto.request.country.countrylocale.CreateCountryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.country.countrylocale.UpdateCountryLocaleRequest;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.CountryLocaleEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.service.CountryLocaleService;
import com.example.resortbackendapplication1.service.CountryService;
import com.example.resortbackendapplication1.service.LocaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries/{country-id}/locales")
public class CountryLocaleController {

    private final CountryService countryService;
    private final CountryLocaleService countryLocaleService;
    private final LocaleService localeService;

    public CountryLocaleController(CountryService countryService,
                                   CountryLocaleService countryLocaleService,
                                   LocaleService localeService) {
        this.countryService = countryService;
        this.countryLocaleService = countryLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("country-id") Long countryId,
            @Valid @RequestBody CreateCountryLocaleRequest request) {
        CountryEntity country = countryService.getEntityById(countryId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(countryLocaleService.create(country, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("country-id") Long countryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCountryLocaleRequest request) {
        CountryLocaleEntity entity = countryLocaleService.getEntityById(countryId, id);
        return ResponseEntity.ok(countryLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("country-id") Long countryId,
            @PathVariable Long id) {
        CountryLocaleEntity entity = countryLocaleService.getEntityById(countryId, id);
        return ResponseEntity.ok(countryLocaleService.delete(entity));
    }
}
