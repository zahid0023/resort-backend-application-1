package com.example.resortbackendapplication1.payment.service;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.CreatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.UpdatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PaymentStatusLocaleService {
    SuccessResponse create(CreatePaymentStatusLocaleRequest request,
                           PaymentStatusEntity paymentStatusEntity,
                           LocaleEntity localeEntity);

    PaymentStatusLocaleEntity getEntityById(Long paymentStatusId, Long id);

    PaginatedResponse<PaymentStatusLocaleDto> getAll(Long paymentStatusId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long paymentStatusId);

    SuccessResponse update(PaymentStatusLocaleEntity entity,
                           UpdatePaymentStatusLocaleRequest request);

    SuccessResponse delete(PaymentStatusLocaleEntity entity);
}
