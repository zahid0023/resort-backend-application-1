package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortFacilityGroupSpecification {

    public Specification<@NonNull ResortFacilityGroupEntity> filter(ResortFacilityGroupFilterRequest request, Long resortId) {
        Specification<ResortFacilityGroupEntity> spec = SpecificationUtils.build(request);
        if (resortId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("resortEntity").get("id"), resortId));
        }
        return spec;
    }
}
