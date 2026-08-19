package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityoperatinghours.SetResortFacilityOperatingHoursScheduleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityOperatingHoursService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities/{facility-id}/operating-hours")
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
            @PathVariable("facility-id") Long facilityId,
            @Valid @RequestBody SetResortFacilityOperatingHoursScheduleRequest request) {
        ResortFacilityEntity resortFacilityEntity = resortFacilityService.getEntityById(resortId, facilityId);
        List<DayOfWeekEntity> allDaysOfWeek = dayOfWeekService.getAllActiveEntities();
        return ResponseEntity.ok(resortFacilityOperatingHoursService.setWeeklySchedule(resortFacilityEntity, request, allDaysOfWeek));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("facility-id") Long facilityId,
            @ParameterObject PaginatedRequest request) {
        resortFacilityService.getEntityById(resortId, facilityId);
        return ResponseEntity.ok(resortFacilityOperatingHoursService.getAll(facilityId, request));
    }
}
