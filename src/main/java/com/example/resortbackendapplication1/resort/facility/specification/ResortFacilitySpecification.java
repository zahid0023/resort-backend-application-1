package com.example.resortbackendapplication1.resort.facility.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortFacilitySpecification {

    public Specification<@NonNull ResortFacilityEntity> filter(Long resortId, ResortFacilityFilterRequest request, Long localeId) {
        Specification<@NonNull ResortFacilityEntity> byResort =
                (root, query, cb) -> cb.equal(root.get("resortEntity").get("id"), resortId);
        return byResort.and(SpecificationUtils.build(request, localeId));
    }
}
