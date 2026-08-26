package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.resort.dto.request.resortweeklyschedule.ResortWeeklyScheduleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resort.service.ResortWeeklyScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/weekly-schedule")
public class ResortWeeklyScheduleController {

    private final ResortWeeklyScheduleService resortWeeklyScheduleService;
    private final ResortService resortService;
    private final PriceTypeService priceTypeService;
    private final DayOfWeekService dayOfWeekService;

    public ResortWeeklyScheduleController(ResortWeeklyScheduleService resortWeeklyScheduleService,
                                          ResortService resortService,
                                          PriceTypeService priceTypeService,
                                          DayOfWeekService dayOfWeekService) {
        this.resortWeeklyScheduleService = resortWeeklyScheduleService;
        this.resortService = resortService;
        this.priceTypeService = priceTypeService;
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
        PriceTypeEntity weekdayPriceTypeEntity = priceTypeService.getEntityByCode("WKD");
        PriceTypeEntity weekendPriceTypeEntity = priceTypeService.getEntityByCode("WKE");
        List<DayOfWeekEntity> weekdayDayOfWeekEntities = resolveDayOfWeekEntities(request.getWeekdayDayOfWeekIds());
        List<DayOfWeekEntity> weekendDayOfWeekEntities = resolveDayOfWeekEntities(request.getWeekendDayOfWeekIds());
        return ResponseEntity.ok(resortWeeklyScheduleService.updateWeeklySchedule(
                resortEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                weekdayDayOfWeekEntities, weekendDayOfWeekEntities));
    }

    private List<DayOfWeekEntity> resolveDayOfWeekEntities(List<Long> dayOfWeekIds) {
        return dayOfWeekIds.stream()
                .map(dayOfWeekService::getEntityById)
                .toList();
    }
}
