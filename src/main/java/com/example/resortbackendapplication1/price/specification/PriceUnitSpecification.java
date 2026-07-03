package com.example.resortbackendapplication1.price.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitFilterRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PriceUnitSpecification {

    public Specification<@NonNull PriceUnitEntity> filter(PriceUnitFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
