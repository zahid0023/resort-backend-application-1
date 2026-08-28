package com.example.resortbackendapplication1.roomstatus.specification;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.RoomStatusFilterRequest;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class RoomStatusSpecification {

    public Specification<@NonNull RoomStatusEntity> filter(RoomStatusFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
