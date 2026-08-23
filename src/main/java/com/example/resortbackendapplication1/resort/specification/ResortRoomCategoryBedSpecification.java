package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.ResortRoomCategoryBedFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryBedEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomCategoryBedSpecification {

    public Specification<@NonNull ResortRoomCategoryBedEntity> filter(Long resortRoomCategoryId, ResortRoomCategoryBedFilterRequest request) {
        Specification<@NonNull ResortRoomCategoryBedEntity> byResortRoomCategory =
                (root, query, cb) -> cb.equal(root.get("resortRoomCategoryEntity").get("id"), resortRoomCategoryId);
        return byResortRoomCategory.and(SpecificationUtils.build(request));
    }
}
