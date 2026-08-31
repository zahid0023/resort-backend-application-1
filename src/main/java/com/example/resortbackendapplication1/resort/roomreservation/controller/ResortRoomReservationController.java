package com.example.resortbackendapplication1.resort.roomreservation.controller;

import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.ResortRoomReservationFilterRequest;
import com.example.resortbackendapplication1.resort.roomreservation.service.ResortRoomReservationService;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The single reservation-listing endpoint — a resort-wide, paginated view across every room/category.
 */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/reservations")
public class ResortRoomReservationController {

    private final ResortRoomReservationService resortRoomReservationService;
    private final ResortService resortService;

    public ResortRoomReservationController(ResortRoomReservationService resortRoomReservationService, ResortService resortService) {
        this.resortRoomReservationService = resortRoomReservationService;
        this.resortService = resortService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortRoomReservationFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortRoomReservationService.getAll(resortId, request));
    }
}
