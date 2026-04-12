package com.example.resortapplication1.controller;

import com.example.resortapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortapplication1.dto.request.countries.CreateCountryRequest;
import com.example.resortapplication1.dto.request.countries.UpdateCountryRequest;
import com.example.resortapplication1.service.CountryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<?> createCountry(@RequestBody CreateCountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.createCountry(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCountry(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getCountry(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllCountries(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "code", "name"));
        return ResponseEntity.ok(countryService.getAllCountries(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCountry(@PathVariable Long id, @RequestBody UpdateCountryRequest request) {
        return ResponseEntity.ok(countryService.updateCountry(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.deleteCountry(id));
    }
}
