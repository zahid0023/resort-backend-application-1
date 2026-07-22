package com.example.resortbackendapplication1.currency.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.currency.dto.request.currency.CurrencyFilterRequest;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CurrencySpecification {

    public Specification<@NonNull CurrencyEntity> filter(CurrencyFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
