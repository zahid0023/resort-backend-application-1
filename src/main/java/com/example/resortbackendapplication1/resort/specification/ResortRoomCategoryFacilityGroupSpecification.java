package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomCategoryFacilityGroupSpecification {

    public Specification<@NonNull ResortRoomCategoryFacilityGroupEntity> filter(Long resortRoomCategoryId, ResortRoomCategoryFacilityGroupFilterRequest request, Long localeId) {
        Specification<@NonNull ResortRoomCategoryFacilityGroupEntity> byResortRoomCategory =
                (root, query, cb) -> cb.equal(root.get("resortRoomCategoryEntity").get("id"), resortRoomCategoryId);
        return byResortRoomCategory.and(SpecificationUtils.build(request, localeId));
    }
}
