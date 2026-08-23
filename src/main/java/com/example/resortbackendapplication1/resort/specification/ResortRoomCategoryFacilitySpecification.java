package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomCategoryFacilitySpecification {

    public Specification<@NonNull ResortRoomCategoryFacilityEntity> filter(Long resortRoomCategoryId, ResortRoomCategoryFacilityFilterRequest request, Long localeId) {
        Specification<@NonNull ResortRoomCategoryFacilityEntity> byResortRoomCategory =
                (root, query, cb) -> cb.equal(root.get("resortRoomCategoryEntity").get("id"), resortRoomCategoryId);
        return byResortRoomCategory.and(SpecificationUtils.build(request, localeId));
    }
}
