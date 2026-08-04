package com.example.resortbackendapplication1.price.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PriceTypeSpecification {

    public Specification<@NonNull PriceTypeEntity> filter(PriceTypeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
