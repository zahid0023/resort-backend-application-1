package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortFacilitySpecification {

    public Specification<@NonNull ResortFacilityEntity> filter(ResortFacilityFilterRequest request, Long resortFacilityGroupId) {
        Specification<ResortFacilityEntity> spec = SpecificationUtils.build(request);
        if (resortFacilityGroupId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("resortFacilityGroupEntity").get("id"), resortFacilityGroupId));
        }
        return spec;
    }
}
