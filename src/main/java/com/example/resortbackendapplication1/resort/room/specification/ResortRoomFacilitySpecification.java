package com.example.resortbackendapplication1.resort.room.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.ResortRoomFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomFacilitySpecification {

    public Specification<@NonNull ResortRoomFacilityEntity> filter(Long resortRoomId, ResortRoomFacilityFilterRequest request, Long localeId) {
        Specification<@NonNull ResortRoomFacilityEntity> byResortRoom =
                (root, query, cb) -> cb.equal(root.get("resortRoomEntity").get("id"), resortRoomId);
        return byResortRoom.and(SpecificationUtils.build(request, localeId));
    }
}
