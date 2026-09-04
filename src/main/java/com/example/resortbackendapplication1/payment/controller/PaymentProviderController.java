package com.example.resortbackendapplication1.payment.controller;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.PaymentProviderFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.CreatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.UpdatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.payment.service.PaymentMethodService;
import com.example.resortbackendapplication1.payment.service.PaymentProviderService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-providers")
public class PaymentProviderController {

    private final PaymentProviderService paymentProviderService;
    private final PaymentMethodService paymentMethodService;
    private final LocaleService localeService;

    public PaymentProviderController(PaymentProviderService paymentProviderService,
                                     PaymentMethodService paymentMethodService,
                                     LocaleService localeService) {
        this.paymentProviderService = paymentProviderService;
        this.paymentMethodService = paymentMethodService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePaymentProviderRequest request) {
        PaymentMethodEntity paymentMethodEntity = paymentMethodService.getEntityById(request.getPaymentMethodId());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentProviderService.create(request, paymentMethodEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentProviderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaymentProviderFilterRequest request) {
        return ResponseEntity.ok(paymentProviderService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentProviderRequest request) {
        PaymentProviderEntity entity = paymentProviderService.getEntityById(id);
        return ResponseEntity.ok(paymentProviderService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        PaymentProviderEntity entity = paymentProviderService.getEntityById(id);
        return ResponseEntity.ok(paymentProviderService.delete(entity));
    }
}
