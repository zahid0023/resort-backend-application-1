package com.example.resortbackendapplication1.payment.controller;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.PaymentMethodFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.CreatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.UpdatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.service.PaymentMethodService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;
    private final LocaleService localeService;

    public PaymentMethodController(PaymentMethodService paymentMethodService,
                                   LocaleService localeService) {
        this.paymentMethodService = paymentMethodService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePaymentMethodRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentMethodService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentMethodService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaymentMethodFilterRequest request) {
        return ResponseEntity.ok(paymentMethodService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentMethodRequest request) {
        PaymentMethodEntity entity = paymentMethodService.getEntityById(id);
        return ResponseEntity.ok(paymentMethodService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        PaymentMethodEntity entity = paymentMethodService.getEntityById(id);
        return ResponseEntity.ok(paymentMethodService.delete(entity));
    }
}
