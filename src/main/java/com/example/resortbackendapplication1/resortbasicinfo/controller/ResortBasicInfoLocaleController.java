package com.example.resortbackendapplication1.resortbasicinfo.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoLocaleEntity;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoLocaleService;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resort-basic-info/locales")
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

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntity();
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortBasicInfoLocaleService.create(resortBasicInfoEntity, localeEntity, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntity();
        ResortBasicInfoLocaleEntity entity = resortBasicInfoLocaleService.getEntityById(resortBasicInfoEntity.getId(), id);
        return ResponseEntity.ok(resortBasicInfoLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntity();
        ResortBasicInfoLocaleEntity entity = resortBasicInfoLocaleService.getEntityById(resortBasicInfoEntity.getId(), id);
        return ResponseEntity.ok(resortBasicInfoLocaleService.delete(entity));
    }
}
