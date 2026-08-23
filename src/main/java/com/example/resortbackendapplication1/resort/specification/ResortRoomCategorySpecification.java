package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomCategorySpecification {

    public Specification<@NonNull ResortRoomCategoryEntity> filter(Long resortId, ResortRoomCategoryFilterRequest request, Long localeId) {
        Specification<@NonNull ResortRoomCategoryEntity> byResort =
                (root, query, cb) -> cb.equal(root.get("resortEntity").get("id"), resortId);
        return byResort.and(SpecificationUtils.build(request, localeId));
    }
}
