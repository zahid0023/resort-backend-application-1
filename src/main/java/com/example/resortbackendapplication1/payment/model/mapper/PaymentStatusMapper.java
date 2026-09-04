package com.example.resortbackendapplication1.payment.model.mapper;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.PaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.CreatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.UpdatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusDto;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PaymentStatusMapper {

    public PaymentStatusEntity create(CreatePaymentStatusRequest request) {
        PaymentStatusEntity entity = new PaymentStatusEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PaymentStatusEntity entity, UpdatePaymentStatusRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PaymentStatusEntity entity, PaymentStatusRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PaymentStatusDto.PaymentStatusDtoBuilder toDto(PaymentStatusEntity entity) {
        return PaymentStatusDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<PaymentStatusLocaleEntity> activeLocales(PaymentStatusEntity entity) {
        return entity.getPaymentStatusLocaleEntities().stream()
                .filter(paymentStatusLocaleEntity -> Boolean.TRUE.equals(paymentStatusLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(paymentStatusLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PaymentStatusLocaleDto singleLocale(PaymentStatusEntity entity) {
        PaymentStatusLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PaymentStatusLocaleMapper.toDto(matched);
    }

    private PaymentStatusLocaleEntity matchLocale(PaymentStatusEntity entity, Long localeId) {
        List<PaymentStatusLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(paymentStatusLocaleEntity -> paymentStatusLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(paymentStatusLocaleEntity -> "en".equals(paymentStatusLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
