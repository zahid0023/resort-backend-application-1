package com.example.resortbackendapplication1.resortroomcategory.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomCategorySpecification {

    public Specification<@NonNull ResortRoomCategoryEntity> filter(ResortRoomCategoryFilterRequest request, Long resortId) {
        Specification<ResortRoomCategoryEntity> spec = SpecificationUtils.build(request);
        return spec.and((root, query, cb) ->
                cb.equal(root.get("resortEntity").get("id"), resortId));
    }
}
