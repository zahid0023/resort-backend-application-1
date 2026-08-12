package com.example.resortbackendapplication1.resortroletype.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.ResortRoleTypeFilterRequest;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoleTypeSpecification {

    public Specification<@NonNull ResortRoleTypeEntity> filter(ResortRoleTypeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
