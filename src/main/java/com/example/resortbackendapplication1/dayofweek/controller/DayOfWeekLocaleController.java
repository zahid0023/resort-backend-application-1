package com.example.resortbackendapplication1.dayofweek.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.CreateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekLocaleService;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/days-of-week/{day-of-week-id}/locales")
public class DayOfWeekLocaleController {

    private final DayOfWeekService dayOfWeekService;
    private final DayOfWeekLocaleService dayOfWeekLocaleService;
    private final LocaleService localeService;

    public DayOfWeekLocaleController(DayOfWeekService dayOfWeekService,
                                     DayOfWeekLocaleService dayOfWeekLocaleService,
                                     LocaleService localeService) {
        this.dayOfWeekService = dayOfWeekService;
        this.dayOfWeekLocaleService = dayOfWeekLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("day-of-week-id") Long dayOfWeekId,
            @Valid @RequestBody CreateDayOfWeekLocaleRequest request) {
        DayOfWeekEntity dayOfWeekEntity = dayOfWeekService.getEntityById(dayOfWeekId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dayOfWeekLocaleService.create(request, dayOfWeekEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("day-of-week-id") Long dayOfWeekId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        dayOfWeekService.getEntityById(dayOfWeekId);
        return ResponseEntity.ok(dayOfWeekLocaleService.getAll(dayOfWeekId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("day-of-week-id") Long dayOfWeekId) {
        dayOfWeekService.getEntityById(dayOfWeekId);
        return ResponseEntity.ok(dayOfWeekLocaleService.getCount(dayOfWeekId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("day-of-week-id") Long dayOfWeekId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateDayOfWeekLocaleRequest request) {
        DayOfWeekLocaleEntity entity = dayOfWeekLocaleService.getEntityById(dayOfWeekId, id);
        return ResponseEntity.ok(dayOfWeekLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("day-of-week-id") Long dayOfWeekId,
            @PathVariable Long id) {
        DayOfWeekLocaleEntity entity = dayOfWeekLocaleService.getEntityById(dayOfWeekId, id);
        return ResponseEntity.ok(dayOfWeekLocaleService.delete(entity));
    }
}
