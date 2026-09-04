package com.example.resortbackendapplication1.payment.specification;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.PaymentStatusFilterRequest;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PaymentStatusSpecification {

    public Specification<@NonNull PaymentStatusEntity> filter(PaymentStatusFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
