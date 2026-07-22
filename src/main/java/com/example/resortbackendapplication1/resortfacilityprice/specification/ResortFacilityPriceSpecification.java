package com.example.resortbackendapplication1.resortfacilityprice.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.ResortFacilityPriceFilterRequest;
import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortFacilityPriceSpecification {

    public Specification<@NonNull ResortFacilityPriceEntity> filter(
            ResortFacilityPriceFilterRequest request, Long resortFacilityId) {
        Specification<ResortFacilityPriceEntity> spec = SpecificationUtils.build(request);
        return spec.and((root, query, cb) ->
                cb.equal(root.get("resortFacilityEntity").get("id"), resortFacilityId));
    }
}
