package com.example.resortbackendapplication1.facility.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.dto.request.facility.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilitySpecification {

    public Specification<@NonNull FacilityEntity> filter(FacilityFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
