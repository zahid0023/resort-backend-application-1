package com.example.resortbackendapplication1.dayofweek.controller;

import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.UpdateDayOfWeekRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/days-of-week")
public class DayOfWeekController {

    private final DayOfWeekService dayOfWeekService;

    public DayOfWeekController(DayOfWeekService dayOfWeekService) {
        this.dayOfWeekService = dayOfWeekService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dayOfWeekService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject DayOfWeekFilterRequest request) {
        return ResponseEntity.ok(dayOfWeekService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDayOfWeekRequest request) {
        DayOfWeekEntity entity = dayOfWeekService.getEntityById(id);
        return ResponseEntity.ok(dayOfWeekService.update(entity, request));
    }


}
