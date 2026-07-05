package com.example.resortbackendapplication1.resortaccesstype.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.ResortAccessTypeFilterRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortAccessTypeSpecification {

    public Specification<@NonNull ResortAccessTypeEntity> filter(ResortAccessTypeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
