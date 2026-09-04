package com.example.resortbackendapplication1.payment.model.mapper;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.PaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.CreatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.UpdatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodDto;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PaymentMethodMapper {

    public PaymentMethodEntity create(CreatePaymentMethodRequest request) {
        PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PaymentMethodEntity entity, UpdatePaymentMethodRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PaymentMethodEntity entity, PaymentMethodRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PaymentMethodDto.PaymentMethodDtoBuilder toDto(PaymentMethodEntity entity) {
        return PaymentMethodDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<PaymentMethodLocaleEntity> activeLocales(PaymentMethodEntity entity) {
        return entity.getPaymentMethodLocaleEntities().stream()
                .filter(paymentMethodLocaleEntity -> Boolean.TRUE.equals(paymentMethodLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(paymentMethodLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PaymentMethodLocaleDto singleLocale(PaymentMethodEntity entity) {
        PaymentMethodLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PaymentMethodLocaleMapper.toDto(matched);
    }

    private PaymentMethodLocaleEntity matchLocale(PaymentMethodEntity entity, Long localeId) {
        List<PaymentMethodLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(paymentMethodLocaleEntity -> paymentMethodLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(paymentMethodLocaleEntity -> "en".equals(paymentMethodLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
