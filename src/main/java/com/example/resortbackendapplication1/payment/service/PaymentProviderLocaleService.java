package com.example.resortbackendapplication1.payment.service;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.CreatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.UpdatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PaymentProviderLocaleService {
    SuccessResponse create(CreatePaymentProviderLocaleRequest request,
                           PaymentProviderEntity paymentProviderEntity,
                           LocaleEntity localeEntity);

    PaymentProviderLocaleEntity getEntityById(Long paymentProviderId, Long id);

    PaginatedResponse<PaymentProviderLocaleDto> getAll(Long paymentProviderId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long paymentProviderId);

    SuccessResponse update(PaymentProviderLocaleEntity entity,
                           UpdatePaymentProviderLocaleRequest request);

    SuccessResponse delete(PaymentProviderLocaleEntity entity);
}
