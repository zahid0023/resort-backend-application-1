package com.example.resortbackendapplication1.resort.room.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.ResortRoomBedFilterRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomBedSpecification {

    public Specification<@NonNull ResortRoomBedEntity> filter(Long resortRoomId, ResortRoomBedFilterRequest request) {
        Specification<@NonNull ResortRoomBedEntity> byResortRoom =
                (root, query, cb) -> cb.equal(root.get("resortRoomEntity").get("id"), resortRoomId);
        return byResortRoom.and(SpecificationUtils.build(request));
    }
}
