package com.example.resortbackendapplication1.resort.room.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.ResortRoomFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomFacilityGroupSpecification {

    public Specification<@NonNull ResortRoomFacilityGroupEntity> filter(Long resortRoomId, ResortRoomFacilityGroupFilterRequest request, Long localeId) {
        Specification<@NonNull ResortRoomFacilityGroupEntity> byResortRoom =
                (root, query, cb) -> cb.equal(root.get("resortRoomEntity").get("id"), resortRoomId);
        return byResortRoom.and(SpecificationUtils.build(request, localeId));
    }
}
