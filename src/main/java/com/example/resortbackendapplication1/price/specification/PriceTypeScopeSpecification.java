package com.example.resortbackendapplication1.price.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.PriceTypeScopeFilterRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PriceTypeScopeSpecification {

    public Specification<@NonNull PriceTypeScopeEntity> filter(PriceTypeScopeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
