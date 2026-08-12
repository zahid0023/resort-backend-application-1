package com.example.resortbackendapplication1.resortpermissiontype.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortPermissionTypeSpecification {

    public Specification<@NonNull ResortPermissionTypeEntity> filter(ResortPermissionTypeFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
