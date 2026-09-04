package com.example.resortbackendapplication1.payment.controller;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.PaymentStatusFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.CreatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.UpdatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.payment.service.PaymentStatusService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-statuses")
public class PaymentStatusController {

    private final PaymentStatusService paymentStatusService;
    private final LocaleService localeService;

    public PaymentStatusController(PaymentStatusService paymentStatusService,
                                   LocaleService localeService) {
        this.paymentStatusService = paymentStatusService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePaymentStatusRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentStatusService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentStatusService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaymentStatusFilterRequest request) {
        return ResponseEntity.ok(paymentStatusService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        PaymentStatusEntity entity = paymentStatusService.getEntityById(id);
        return ResponseEntity.ok(paymentStatusService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        PaymentStatusEntity entity = paymentStatusService.getEntityById(id);
        return ResponseEntity.ok(paymentStatusService.delete(entity));
    }
}
