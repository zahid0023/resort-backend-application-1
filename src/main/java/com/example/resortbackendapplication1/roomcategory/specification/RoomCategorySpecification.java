package com.example.resortbackendapplication1.roomcategory.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryFilterRequest;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class RoomCategorySpecification {

    public Specification<@NonNull RoomCategoryEntity> filter(RoomCategoryFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
