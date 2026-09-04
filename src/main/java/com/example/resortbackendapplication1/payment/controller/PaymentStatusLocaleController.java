package com.example.resortbackendapplication1.payment.controller;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.CreatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.UpdatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.payment.service.PaymentStatusLocaleService;
import com.example.resortbackendapplication1.payment.service.PaymentStatusService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-statuses/{payment-status-id}/locales")
public class PaymentStatusLocaleController {

    private final PaymentStatusService paymentStatusService;
    private final PaymentStatusLocaleService paymentStatusLocaleService;
    private final LocaleService localeService;

    public PaymentStatusLocaleController(PaymentStatusService paymentStatusService,
                                         PaymentStatusLocaleService paymentStatusLocaleService,
                                         LocaleService localeService) {
        this.paymentStatusService = paymentStatusService;
        this.paymentStatusLocaleService = paymentStatusLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("payment-status-id") Long paymentStatusId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        paymentStatusService.getEntityById(paymentStatusId);
        return ResponseEntity.ok(paymentStatusLocaleService.getAll(paymentStatusId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("payment-status-id") Long paymentStatusId,
            @Valid @RequestBody CreatePaymentStatusLocaleRequest request) {
        PaymentStatusEntity paymentStatusEntity = paymentStatusService.getEntityById(paymentStatusId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentStatusLocaleService.create(request, paymentStatusEntity, localeEntity));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("payment-status-id") Long paymentStatusId) {
        paymentStatusService.getEntityById(paymentStatusId);
        return ResponseEntity.ok(paymentStatusLocaleService.getCount(paymentStatusId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("payment-status-id") Long paymentStatusId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusLocaleRequest request) {
        PaymentStatusLocaleEntity entity = paymentStatusLocaleService.getEntityById(paymentStatusId, id);
        return ResponseEntity.ok(paymentStatusLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("payment-status-id") Long paymentStatusId,
            @PathVariable Long id) {
        PaymentStatusLocaleEntity entity = paymentStatusLocaleService.getEntityById(paymentStatusId, id);
        return ResponseEntity.ok(paymentStatusLocaleService.delete(entity));
    }
}
