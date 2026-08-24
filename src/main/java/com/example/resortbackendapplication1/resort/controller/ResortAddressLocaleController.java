package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.locale.CreateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.locale.UpdateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortAddressLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortAddressService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/address/locales")
public class ResortAddressLocaleController {

    private final ResortAddressService resortAddressService;
    private final ResortAddressLocaleService resortAddressLocaleService;
    private final LocaleService localeService;

    public ResortAddressLocaleController(ResortAddressService resortAddressService,
                                         ResortAddressLocaleService resortAddressLocaleService,
                                         LocaleService localeService) {
        this.resortAddressService = resortAddressService;
        this.resortAddressLocaleService = resortAddressLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(resortId);
        return ResponseEntity.ok(resortAddressLocaleService.getAll(resortAddressEntity.getId(), localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(@PathVariable("resort-id") Long resortId) {
        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(resortId);
        return ResponseEntity.ok(resortAddressLocaleService.getActiveCount(resortAddressEntity.getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortAddressLocaleRequest request) {
        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(resortId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortAddressLocaleService.create(request, resortAddressEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortAddressLocaleRequest request) {
        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(resortId);
        ResortAddressLocaleEntity entity = resortAddressLocaleService.getEntityById(resortAddressEntity.getId(), id);
        return ResponseEntity.ok(resortAddressLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(resortId);
        ResortAddressLocaleEntity entity = resortAddressLocaleService.getEntityById(resortAddressEntity.getId(), id);
        return ResponseEntity.ok(resortAddressLocaleService.delete(entity));
    }
}
