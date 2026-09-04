package com.example.resortbackendapplication1.payment.service;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.PaymentStatusFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.CreatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.UpdatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.response.paymentstatuses.PaymentStatusResponse;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PaymentStatusService {

    SuccessResponse create(CreatePaymentStatusRequest request,
                           LocaleEntity localeEntity);

    PaymentStatusEntity getEntityById(Long id);

    PaymentStatusResponse getById(Long id);

    PaginatedResponse<PaymentStatusDto> getAll(PaymentStatusFilterRequest request);

    SuccessResponse update(PaymentStatusEntity entity,
                           UpdatePaymentStatusRequest request);

    SuccessResponse delete(PaymentStatusEntity entity);
}
