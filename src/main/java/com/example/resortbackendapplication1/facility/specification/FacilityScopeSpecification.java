package com.example.resortbackendapplication1.facility.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilityScopeSpecification {

    public Specification<@NonNull FacilityScopeEntity> filter(FacilityScopeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
