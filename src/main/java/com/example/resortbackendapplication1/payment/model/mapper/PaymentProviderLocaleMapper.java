package com.example.resortbackendapplication1.payment.model.mapper;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.PaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.UpdatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentProviderLocaleMapper {

    public PaymentProviderLocaleEntity create(PaymentProviderLocaleRequest request) {
        PaymentProviderLocaleEntity entity = new PaymentProviderLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PaymentProviderLocaleEntity entity, UpdatePaymentProviderLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PaymentProviderLocaleEntity entity, PaymentProviderLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public PaymentProviderLocaleDto toDto(PaymentProviderLocaleEntity entity) {
        return PaymentProviderLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
