package com.example.resortbackendapplication1.price.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.price.dto.request.pricescope.PriceScopeFilterRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PriceScopeSpecification {

    public Specification<@NonNull PriceScopeEntity> filter(PriceScopeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
