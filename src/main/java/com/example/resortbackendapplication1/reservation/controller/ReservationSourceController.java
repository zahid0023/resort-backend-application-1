package com.example.resortbackendapplication1.reservation.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.CreateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.ReservationSourceFilterRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.UpdateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservation-sources")
public class ReservationSourceController {

    private final ReservationSourceService reservationSourceService;
    private final LocaleService localeService;

    public ReservationSourceController(ReservationSourceService reservationSourceService,
                                       LocaleService localeService) {
        this.reservationSourceService = reservationSourceService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateReservationSourceRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationSourceService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationSourceService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ReservationSourceFilterRequest request) {
        return ResponseEntity.ok(reservationSourceService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationSourceRequest request) {
        ReservationSourceEntity entity = reservationSourceService.getEntityById(id);
        return ResponseEntity.ok(reservationSourceService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ReservationSourceEntity entity = reservationSourceService.getEntityById(id);
        return ResponseEntity.ok(reservationSourceService.delete(entity));
    }
}
