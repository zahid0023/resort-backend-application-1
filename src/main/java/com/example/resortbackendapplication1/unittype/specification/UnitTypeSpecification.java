package com.example.resortbackendapplication1.unittype.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UnitTypeFilterRequest;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UnitTypeSpecification {

    public Specification<@NonNull UnitTypeEntity> filter(UnitTypeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
