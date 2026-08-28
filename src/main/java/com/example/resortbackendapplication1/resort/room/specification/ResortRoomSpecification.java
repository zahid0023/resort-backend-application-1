package com.example.resortbackendapplication1.resort.room.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomSpecification {

    public Specification<@NonNull ResortRoomEntity> filter(Long resortRoomCategoryId, ResortRoomFilterRequest request, Long localeId) {
        Specification<@NonNull ResortRoomEntity> byResortRoomCategory =
                (root, query, cb) -> cb.equal(root.get("resortRoomCategoryEntity").get("id"), resortRoomCategoryId);
        return byResortRoomCategory.and(SpecificationUtils.build(request, localeId));
    }
}
