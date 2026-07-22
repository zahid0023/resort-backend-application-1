package com.example.resortbackendapplication1.bedtype.specification;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeFilterRequest;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class BedTypeSpecification {

    public Specification<@NonNull BedTypeEntity> filter(BedTypeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
