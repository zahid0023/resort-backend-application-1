package com.example.resortbackendapplication1.payment.model.mapper;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.PaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.CreatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.UpdatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderDto;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PaymentProviderMapper {

    public PaymentProviderEntity create(CreatePaymentProviderRequest request) {
        PaymentProviderEntity entity = new PaymentProviderEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PaymentProviderEntity entity, UpdatePaymentProviderRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PaymentProviderEntity entity, PaymentProviderRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public PaymentProviderDto.PaymentProviderDtoBuilder toDto(PaymentProviderEntity entity) {
        return PaymentProviderDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<PaymentProviderLocaleEntity> activeLocales(PaymentProviderEntity entity) {
        return entity.getPaymentProviderLocaleEntities().stream()
                .filter(paymentProviderLocaleEntity -> Boolean.TRUE.equals(paymentProviderLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(paymentProviderLocaleEntity.getIsDeleted()))
                .toList();
    }

    private PaymentProviderLocaleDto singleLocale(PaymentProviderEntity entity) {
        PaymentProviderLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : PaymentProviderLocaleMapper.toDto(matched);
    }

    private PaymentProviderLocaleEntity matchLocale(PaymentProviderEntity entity, Long localeId) {
        List<PaymentProviderLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(paymentProviderLocaleEntity -> paymentProviderLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(paymentProviderLocaleEntity -> "en".equals(paymentProviderLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
