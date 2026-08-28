package com.example.resortbackendapplication1.resort.facility.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortFacilityGroupSpecification {

    public Specification<@NonNull ResortFacilityGroupEntity> filter(Long resortId, ResortFacilityGroupFilterRequest request, Long localeId) {
        Specification<@NonNull ResortFacilityGroupEntity> byResort =
                (root, query, cb) -> cb.equal(root.get("resortEntity").get("id"), resortId);
        return byResort.and(SpecificationUtils.build(request, localeId));
    }
}
