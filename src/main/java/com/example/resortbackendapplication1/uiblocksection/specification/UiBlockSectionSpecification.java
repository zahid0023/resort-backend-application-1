package com.example.resortbackendapplication1.uiblocksection.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UiBlockSectionFilterRequest;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UiBlockSectionSpecification {

    public Specification<@NonNull UiBlockSectionEntity> filter(UiBlockSectionFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
