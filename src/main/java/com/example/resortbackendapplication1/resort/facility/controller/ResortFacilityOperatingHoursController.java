package com.example.resortbackendapplication1.resort.facility.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.SetResortFacilityOperatingHoursScheduleRequest;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityOperatingHoursService;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/operating-hours")
public class ResortFacilityOperatingHoursController {

    private final ResortFacilityOperatingHoursService resortFacilityOperatingHoursService;
    private final ResortFacilityService resortFacilityService;
    private final DayOfWeekService dayOfWeekService;

    public ResortFacilityOperatingHoursController(
            ResortFacilityOperatingHoursService resortFacilityOperatingHoursService,
            ResortFacilityService resortFacilityService,
            DayOfWeekService dayOfWeekService) {
        this.resortFacilityOperatingHoursService = resortFacilityOperatingHoursService;
        this.resortFacilityService = resortFacilityService;
        this.dayOfWeekService = dayOfWeekService;
    }

    @PutMapping("/schedule")
    public ResponseEntity<?> setWeeklySchedule(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @Valid @RequestBody SetResortFacilityOperatingHoursScheduleRequest request) {
        ResortFacilityEntity resortFacilityEntity = resortFacilityService.getEntityById(resortId, resortFacilityId);
        List<DayOfWeekEntity> allDaysOfWeek = dayOfWeekService.getAllActiveEntities();
        return ResponseEntity.ok(resortFacilityOperatingHoursService.setWeeklySchedule(resortFacilityEntity, request, allDaysOfWeek));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-facility-id") Long resortFacilityId,
            @ParameterObject PaginatedRequest request) {
        resortFacilityService.getEntityById(resortId, resortFacilityId);
        return ResponseEntity.ok(resortFacilityOperatingHoursService.getAll(resortFacilityId, request));
    }
}
