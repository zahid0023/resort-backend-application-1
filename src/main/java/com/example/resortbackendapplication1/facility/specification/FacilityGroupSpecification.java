package com.example.resortbackendapplication1.facility.specification;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilityGroupSpecification {

    public Specification<@NonNull FacilityGroupEntity> filter(FacilityGroupFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
