package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.locale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.locale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoLocaleEntity;
import com.example.resortbackendapplication1.resort.service.ResortBasicInfoLocaleService;
import com.example.resortbackendapplication1.resort.service.ResortBasicInfoService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/basic-info/locales")
public class ResortBasicInfoLocaleController {

    private final ResortBasicInfoService resortBasicInfoService;
    private final ResortBasicInfoLocaleService resortBasicInfoLocaleService;
    private final LocaleService localeService;

    public ResortBasicInfoLocaleController(ResortBasicInfoService resortBasicInfoService,
                                           ResortBasicInfoLocaleService resortBasicInfoLocaleService,
                                           LocaleService localeService) {
        this.resortBasicInfoService = resortBasicInfoService;
        this.resortBasicInfoLocaleService = resortBasicInfoLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntityByResortId(resortId);
        return ResponseEntity.ok(resortBasicInfoLocaleService.getAll(resortBasicInfoEntity.getId(), localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntityByResortId(resortId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortBasicInfoLocaleService.create(request, resortBasicInfoEntity, localeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntityByResortId(resortId);
        ResortBasicInfoLocaleEntity entity = resortBasicInfoLocaleService.getEntityById(resortBasicInfoEntity.getId(), id);
        return ResponseEntity.ok(resortBasicInfoLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntityByResortId(resortId);
        ResortBasicInfoLocaleEntity entity = resortBasicInfoLocaleService.getEntityById(resortBasicInfoEntity.getId(), id);
        return ResponseEntity.ok(resortBasicInfoLocaleService.delete(entity));
    }
}
