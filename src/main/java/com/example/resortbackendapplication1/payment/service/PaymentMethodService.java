package com.example.resortbackendapplication1.payment.service;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.PaymentMethodFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.CreatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.UpdatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.response.paymentmethods.PaymentMethodResponse;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PaymentMethodService {

    SuccessResponse create(CreatePaymentMethodRequest request,
                           LocaleEntity localeEntity);

    PaymentMethodEntity getEntityById(Long id);

    PaymentMethodResponse getById(Long id);

    PaginatedResponse<PaymentMethodDto> getAll(PaymentMethodFilterRequest request);

    SuccessResponse update(PaymentMethodEntity entity,
                           UpdatePaymentMethodRequest request);

    SuccessResponse delete(PaymentMethodEntity entity);
}
