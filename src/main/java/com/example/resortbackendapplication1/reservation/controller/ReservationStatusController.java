package com.example.resortbackendapplication1.reservation.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.CreateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.ReservationStatusFilterRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.UpdateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservation-statuses")
public class ReservationStatusController {

    private final ReservationStatusService reservationStatusService;
    private final LocaleService localeService;

    public ReservationStatusController(ReservationStatusService reservationStatusService,
                                       LocaleService localeService) {
        this.reservationStatusService = reservationStatusService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateReservationStatusRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationStatusService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationStatusService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ReservationStatusFilterRequest request) {
        return ResponseEntity.ok(reservationStatusService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationStatusRequest request) {
        ReservationStatusEntity entity = reservationStatusService.getEntityById(id);
        return ResponseEntity.ok(reservationStatusService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ReservationStatusEntity entity = reservationStatusService.getEntityById(id);
        return ResponseEntity.ok(reservationStatusService.delete(entity));
    }
}
