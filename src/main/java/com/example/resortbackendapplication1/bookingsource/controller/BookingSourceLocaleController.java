package com.example.resortbackendapplication1.bookingsource.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.CreateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.UpdateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import com.example.resortbackendapplication1.bookingsource.service.BookingSourceLocaleService;
import com.example.resortbackendapplication1.bookingsource.service.BookingSourceService;
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
@RequestMapping("/api/v1/booking-sources/{booking-source-id}/locales")
public class BookingSourceLocaleController {

    private final BookingSourceService bookingSourceService;
    private final BookingSourceLocaleService bookingSourceLocaleService;
    private final LocaleService localeService;

    public BookingSourceLocaleController(BookingSourceService bookingSourceService,
                                             BookingSourceLocaleService bookingSourceLocaleService,
                                             LocaleService localeService) {
        this.bookingSourceService = bookingSourceService;
        this.bookingSourceLocaleService = bookingSourceLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("booking-source-id") Long bookingSourceId,
            @Valid @RequestBody CreateBookingSourceLocaleRequest request) {
        BookingSourceEntity bookingSourceEntity = bookingSourceService.getEntityById(bookingSourceId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingSourceLocaleService.create(request, bookingSourceEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("booking-source-id") Long bookingSourceId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        bookingSourceService.getEntityById(bookingSourceId);
        return ResponseEntity.ok(bookingSourceLocaleService.getAll(bookingSourceId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("booking-source-id") Long bookingSourceId) {
        bookingSourceService.getEntityById(bookingSourceId);
        return ResponseEntity.ok(bookingSourceLocaleService.getCount(bookingSourceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("booking-source-id") Long bookingSourceId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingSourceLocaleRequest request) {
        BookingSourceLocaleEntity entity = bookingSourceLocaleService.getEntityById(bookingSourceId, id);
        return ResponseEntity.ok(bookingSourceLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("booking-source-id") Long bookingSourceId,
            @PathVariable Long id) {
        BookingSourceLocaleEntity entity = bookingSourceLocaleService.getEntityById(bookingSourceId, id);
        return ResponseEntity.ok(bookingSourceLocaleService.delete(entity));
    }
}
