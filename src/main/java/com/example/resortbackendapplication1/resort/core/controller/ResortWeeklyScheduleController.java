package com.example.resortbackendapplication1.resort.core.controller;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.resort.core.dto.request.resortweeklyschedule.ResortWeeklyScheduleRequest;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.resort.core.service.ResortWeeklyScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/weekly-schedule")
public class ResortWeeklyScheduleController {

    private final ResortWeeklyScheduleService resortWeeklyScheduleService;
    private final ResortService resortService;
    private final DayOfWeekService dayOfWeekService;

    public ResortWeeklyScheduleController(ResortWeeklyScheduleService resortWeeklyScheduleService,
                                          ResortService resortService,
                                          DayOfWeekService dayOfWeekService) {
        this.resortWeeklyScheduleService = resortWeeklyScheduleService;
        this.resortService = resortService;
        this.dayOfWeekService = dayOfWeekService;
    }

    @GetMapping
    public ResponseEntity<?> getWeeklySchedule(@PathVariable("resort-id") Long resortId) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortWeeklyScheduleService.getWeeklySchedule(resortEntity));
    }

    @PutMapping
    public ResponseEntity<?> updateWeeklySchedule(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody ResortWeeklyScheduleRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        List<DayOfWeekEntity> weekdayDayOfWeekEntities = resolveDayOfWeekEntities(request.getWeekdayDayOfWeekIds());
        List<DayOfWeekEntity> weekendDayOfWeekEntities = resolveDayOfWeekEntities(request.getWeekendDayOfWeekIds());
        return ResponseEntity.ok(resortWeeklyScheduleService.updateWeeklySchedule(
                resortEntity, weekdayDayOfWeekEntities, weekendDayOfWeekEntities));
    }

    private List<DayOfWeekEntity> resolveDayOfWeekEntities(List<Long> dayOfWeekIds) {
        return dayOfWeekIds.stream()
                .map(dayOfWeekService::getEntityById)
                .toList();
    }
}
