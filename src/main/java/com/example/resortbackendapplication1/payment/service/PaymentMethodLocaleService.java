package com.example.resortbackendapplication1.payment.service;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.CreatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.UpdatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PaymentMethodLocaleService {
    SuccessResponse create(CreatePaymentMethodLocaleRequest request,
                           PaymentMethodEntity paymentMethodEntity,
                           LocaleEntity localeEntity);

    PaymentMethodLocaleEntity getEntityById(Long paymentMethodId, Long id);

    PaginatedResponse<PaymentMethodLocaleDto> getAll(Long paymentMethodId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long paymentMethodId);

    SuccessResponse update(PaymentMethodLocaleEntity entity,
                           UpdatePaymentMethodLocaleRequest request);

    SuccessResponse delete(PaymentMethodLocaleEntity entity);
}
