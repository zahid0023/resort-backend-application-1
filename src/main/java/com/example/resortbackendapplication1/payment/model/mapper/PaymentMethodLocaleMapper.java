package com.example.resortbackendapplication1.payment.model.mapper;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.PaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.UpdatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentMethodLocaleMapper {

    public PaymentMethodLocaleEntity create(PaymentMethodLocaleRequest request) {
        PaymentMethodLocaleEntity entity = new PaymentMethodLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PaymentMethodLocaleEntity entity, UpdatePaymentMethodLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PaymentMethodLocaleEntity entity, PaymentMethodLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public PaymentMethodLocaleDto toDto(PaymentMethodLocaleEntity entity) {
        return PaymentMethodLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
