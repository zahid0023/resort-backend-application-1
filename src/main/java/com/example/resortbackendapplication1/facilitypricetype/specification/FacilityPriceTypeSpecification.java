package com.example.resortbackendapplication1.facilitypricetype.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilityPriceTypeSpecification {

    public Specification<@NonNull FacilityPriceTypeEntity> filter(FacilityPriceTypeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
