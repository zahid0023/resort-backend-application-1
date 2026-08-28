package com.example.resortbackendapplication1.price.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilityPriceTypeSpecification {

    public Specification<@NonNull FacilityPriceTypeEntity> filter(FacilityPriceTypeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
