package com.example.resortbackendapplication1.payment.controller;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.CreatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.UpdatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.payment.service.PaymentMethodLocaleService;
import com.example.resortbackendapplication1.payment.service.PaymentMethodService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-methods/{payment-method-id}/locales")
public class PaymentMethodLocaleController {

    private final PaymentMethodService paymentMethodService;
    private final PaymentMethodLocaleService paymentMethodLocaleService;
    private final LocaleService localeService;

    public PaymentMethodLocaleController(PaymentMethodService paymentMethodService,
                                         PaymentMethodLocaleService paymentMethodLocaleService,
                                         LocaleService localeService) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodLocaleService = paymentMethodLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("payment-method-id") Long paymentMethodId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        paymentMethodService.getEntityById(paymentMethodId);
        return ResponseEntity.ok(paymentMethodLocaleService.getAll(paymentMethodId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("payment-method-id") Long paymentMethodId,
            @Valid @RequestBody CreatePaymentMethodLocaleRequest request) {
        PaymentMethodEntity paymentMethodEntity = paymentMethodService.getEntityById(paymentMethodId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentMethodLocaleService.create(request, paymentMethodEntity, localeEntity));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("payment-method-id") Long paymentMethodId) {
        paymentMethodService.getEntityById(paymentMethodId);
        return ResponseEntity.ok(paymentMethodLocaleService.getCount(paymentMethodId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("payment-method-id") Long paymentMethodId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentMethodLocaleRequest request) {
        PaymentMethodLocaleEntity entity = paymentMethodLocaleService.getEntityById(paymentMethodId, id);
        return ResponseEntity.ok(paymentMethodLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("payment-method-id") Long paymentMethodId,
            @PathVariable Long id) {
        PaymentMethodLocaleEntity entity = paymentMethodLocaleService.getEntityById(paymentMethodId, id);
        return ResponseEntity.ok(paymentMethodLocaleService.delete(entity));
    }
}
