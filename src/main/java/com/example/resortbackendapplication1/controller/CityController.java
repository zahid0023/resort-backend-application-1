package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.cities.CreateCityRequest;
import com.example.resortbackendapplication1.dto.request.cities.UpdateCityRequest;
import com.example.resortbackendapplication1.service.CityService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<?> createCity(@RequestBody CreateCityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCity(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllCities(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "name"));
        return ResponseEntity.ok(cityService.getAllCities(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCity(@PathVariable Long id, @RequestBody UpdateCityRequest request) {
        return ResponseEntity.ok(cityService.updateCity(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.deleteCity(id));
    }
}
