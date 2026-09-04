package com.example.resortbackendapplication1.payment.service;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.PaymentProviderFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.CreatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.UpdatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.response.paymentproviders.PaymentProviderResponse;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PaymentProviderService {

    SuccessResponse create(CreatePaymentProviderRequest request,
                           PaymentMethodEntity paymentMethodEntity,
                           LocaleEntity localeEntity);

    PaymentProviderEntity getEntityById(Long id);

    PaymentProviderResponse getById(Long id);

    PaginatedResponse<PaymentProviderDto> getAll(PaymentProviderFilterRequest request);

    SuccessResponse update(PaymentProviderEntity entity,
                           UpdatePaymentProviderRequest request);

    SuccessResponse delete(PaymentProviderEntity entity);
}
