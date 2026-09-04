package com.example.resortbackendapplication1.payment.specification;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.PaymentProviderFilterRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PaymentProviderSpecification {

    public Specification<@NonNull PaymentProviderEntity> filter(PaymentProviderFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
