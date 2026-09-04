package com.example.resortbackendapplication1.payment.model.mapper;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.PaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.UpdatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentStatusLocaleMapper {

    public PaymentStatusLocaleEntity create(PaymentStatusLocaleRequest request) {
        PaymentStatusLocaleEntity entity = new PaymentStatusLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PaymentStatusLocaleEntity entity, UpdatePaymentStatusLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PaymentStatusLocaleEntity entity, PaymentStatusLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public PaymentStatusLocaleDto toDto(PaymentStatusLocaleEntity entity) {
        return PaymentStatusLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
