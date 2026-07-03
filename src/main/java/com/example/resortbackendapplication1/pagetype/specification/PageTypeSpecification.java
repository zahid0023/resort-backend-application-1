package com.example.resortbackendapplication1.pagetype.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.PageTypeFilterRequest;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class PageTypeSpecification {

    public Specification<@NonNull PageTypeEntity> filter(PageTypeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
