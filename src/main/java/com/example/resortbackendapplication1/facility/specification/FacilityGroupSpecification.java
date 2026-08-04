package com.example.resortbackendapplication1.facility.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilityGroupSpecification {

    public Specification<@NonNull FacilityGroupEntity> filter(FacilityGroupFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
