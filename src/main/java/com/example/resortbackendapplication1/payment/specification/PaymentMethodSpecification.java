package com.example.resortbackendapplication1.payment.specification;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.PaymentMethodFilterRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PaymentMethodSpecification {

    public Specification<@NonNull PaymentMethodEntity> filter(PaymentMethodFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
