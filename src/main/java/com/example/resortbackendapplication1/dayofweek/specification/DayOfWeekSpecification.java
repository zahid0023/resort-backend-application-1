package com.example.resortbackendapplication1.dayofweek.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class DayOfWeekSpecification {

    public Specification<@NonNull DayOfWeekEntity> filter(DayOfWeekFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
