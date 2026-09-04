package com.example.resortbackendapplication1.payment.controller;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.CreatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.UpdatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.payment.service.PaymentProviderLocaleService;
import com.example.resortbackendapplication1.payment.service.PaymentProviderService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-providers/{payment-provider-id}/locales")
public class PaymentProviderLocaleController {

    private final PaymentProviderService paymentProviderService;
    private final PaymentProviderLocaleService paymentProviderLocaleService;
    private final LocaleService localeService;

    public PaymentProviderLocaleController(PaymentProviderService paymentProviderService,
                                           PaymentProviderLocaleService paymentProviderLocaleService,
                                           LocaleService localeService) {
        this.paymentProviderService = paymentProviderService;
        this.paymentProviderLocaleService = paymentProviderLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("payment-provider-id") Long paymentProviderId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        paymentProviderService.getEntityById(paymentProviderId);
        return ResponseEntity.ok(paymentProviderLocaleService.getAll(paymentProviderId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("payment-provider-id") Long paymentProviderId,
            @Valid @RequestBody CreatePaymentProviderLocaleRequest request) {
        PaymentProviderEntity paymentProviderEntity = paymentProviderService.getEntityById(paymentProviderId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentProviderLocaleService.create(request, paymentProviderEntity, localeEntity));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("payment-provider-id") Long paymentProviderId) {
        paymentProviderService.getEntityById(paymentProviderId);
        return ResponseEntity.ok(paymentProviderLocaleService.getCount(paymentProviderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("payment-provider-id") Long paymentProviderId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentProviderLocaleRequest request) {
        PaymentProviderLocaleEntity entity = paymentProviderLocaleService.getEntityById(paymentProviderId, id);
        return ResponseEntity.ok(paymentProviderLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("payment-provider-id") Long paymentProviderId,
            @PathVariable Long id) {
        PaymentProviderLocaleEntity entity = paymentProviderLocaleService.getEntityById(paymentProviderId, id);
        return ResponseEntity.ok(paymentProviderLocaleService.delete(entity));
    }
}
