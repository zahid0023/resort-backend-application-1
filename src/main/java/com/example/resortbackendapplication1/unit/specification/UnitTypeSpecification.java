package com.example.resortbackendapplication1.unit.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.unit.dto.request.unittype.UnitTypeFilterRequest;
import com.example.resortbackendapplication1.unit.model.entity.UnitTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UnitTypeSpecification {

    public Specification<@NonNull UnitTypeEntity> filter(UnitTypeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
