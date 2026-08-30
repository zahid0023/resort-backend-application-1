package com.example.resortbackendapplication1.resort.reservation.controller;

import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.ReservationFilterRequest;
import com.example.resortbackendapplication1.resort.reservation.service.ReservationService;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Broader, resort-wide view across every room/category — see ReservationController for the room-scoped CRUD. */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/reservations")
public class ResortReservationController {

    private final ReservationService reservationService;
    private final ResortService resortService;

    public ResortReservationController(ReservationService reservationService, ResortService resortService) {
        this.reservationService = reservationService;
        this.resortService = resortService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ReservationFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(reservationService.getAllForResort(resortId, request));
    }
}
