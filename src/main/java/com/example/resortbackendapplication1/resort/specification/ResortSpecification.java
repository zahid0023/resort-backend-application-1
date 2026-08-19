package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.resort.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortSpecification {

    public Specification<@NonNull ResortEntity> filter(ResortFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
