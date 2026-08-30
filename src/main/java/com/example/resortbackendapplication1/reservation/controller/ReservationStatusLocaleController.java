package com.example.resortbackendapplication1.reservation.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.CreateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.UpdateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusLocaleService;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservation-statuses/{reservation-status-id}/locales")
public class ReservationStatusLocaleController {

    private final ReservationStatusService reservationStatusService;
    private final ReservationStatusLocaleService reservationStatusLocaleService;
    private final LocaleService localeService;

    public ReservationStatusLocaleController(ReservationStatusService reservationStatusService,
                                             ReservationStatusLocaleService reservationStatusLocaleService,
                                             LocaleService localeService) {
        this.reservationStatusService = reservationStatusService;
        this.reservationStatusLocaleService = reservationStatusLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("reservation-status-id") Long reservationStatusId,
            @Valid @RequestBody CreateReservationStatusLocaleRequest request) {
        ReservationStatusEntity reservationStatusEntity = reservationStatusService.getEntityById(reservationStatusId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationStatusLocaleService.create(request, reservationStatusEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("reservation-status-id") Long reservationStatusId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        reservationStatusService.getEntityById(reservationStatusId);
        return ResponseEntity.ok(reservationStatusLocaleService.getAll(reservationStatusId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("reservation-status-id") Long reservationStatusId) {
        reservationStatusService.getEntityById(reservationStatusId);
        return ResponseEntity.ok(reservationStatusLocaleService.getCount(reservationStatusId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("reservation-status-id") Long reservationStatusId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationStatusLocaleRequest request) {
        ReservationStatusLocaleEntity entity = reservationStatusLocaleService.getEntityById(reservationStatusId, id);
        return ResponseEntity.ok(reservationStatusLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("reservation-status-id") Long reservationStatusId,
            @PathVariable Long id) {
        ReservationStatusLocaleEntity entity = reservationStatusLocaleService.getEntityById(reservationStatusId, id);
        return ResponseEntity.ok(reservationStatusLocaleService.delete(entity));
    }
}
