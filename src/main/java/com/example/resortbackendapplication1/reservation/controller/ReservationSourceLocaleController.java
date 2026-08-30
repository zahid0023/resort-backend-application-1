package com.example.resortbackendapplication1.reservation.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.CreateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.UpdateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationSourceLocaleService;
import com.example.resortbackendapplication1.reservation.service.ReservationSourceService;
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
@RequestMapping("/api/v1/reservation-sources/{reservation-source-id}/locales")
public class ReservationSourceLocaleController {

    private final ReservationSourceService reservationSourceService;
    private final ReservationSourceLocaleService reservationSourceLocaleService;
    private final LocaleService localeService;

    public ReservationSourceLocaleController(ReservationSourceService reservationSourceService,
                                             ReservationSourceLocaleService reservationSourceLocaleService,
                                             LocaleService localeService) {
        this.reservationSourceService = reservationSourceService;
        this.reservationSourceLocaleService = reservationSourceLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("reservation-source-id") Long reservationSourceId,
            @Valid @RequestBody CreateReservationSourceLocaleRequest request) {
        ReservationSourceEntity reservationSourceEntity = reservationSourceService.getEntityById(reservationSourceId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationSourceLocaleService.create(request, reservationSourceEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("reservation-source-id") Long reservationSourceId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        reservationSourceService.getEntityById(reservationSourceId);
        return ResponseEntity.ok(reservationSourceLocaleService.getAll(reservationSourceId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("reservation-source-id") Long reservationSourceId) {
        reservationSourceService.getEntityById(reservationSourceId);
        return ResponseEntity.ok(reservationSourceLocaleService.getCount(reservationSourceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("reservation-source-id") Long reservationSourceId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationSourceLocaleRequest request) {
        ReservationSourceLocaleEntity entity = reservationSourceLocaleService.getEntityById(reservationSourceId, id);
        return ResponseEntity.ok(reservationSourceLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("reservation-source-id") Long reservationSourceId,
            @PathVariable Long id) {
        ReservationSourceLocaleEntity entity = reservationSourceLocaleService.getEntityById(reservationSourceId, id);
        return ResponseEntity.ok(reservationSourceLocaleService.delete(entity));
    }
}
