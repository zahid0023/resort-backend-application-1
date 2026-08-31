package com.example.resortbackendapplication1.bookingsource.controller;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.CreateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.BookingSourceFilterRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.UpdateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking-sources")
public class BookingSourceController {

    private final BookingSourceService bookingSourceService;
    private final LocaleService localeService;

    public BookingSourceController(BookingSourceService bookingSourceService,
                                       LocaleService localeService) {
        this.bookingSourceService = bookingSourceService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBookingSourceRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingSourceService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingSourceService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject BookingSourceFilterRequest request) {
        return ResponseEntity.ok(bookingSourceService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingSourceRequest request) {
        BookingSourceEntity entity = bookingSourceService.getEntityById(id);
        return ResponseEntity.ok(bookingSourceService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        BookingSourceEntity entity = bookingSourceService.getEntityById(id);
        return ResponseEntity.ok(bookingSourceService.delete(entity));
    }
}
