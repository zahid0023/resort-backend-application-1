package com.example.resortbackendapplication1.resortcontact.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortcontact.dto.request.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortContactSpecification {

    public Specification<@NonNull ResortContactEntity> filter(ResortContactFilterRequest request, Long resortId) {
        Specification<ResortContactEntity> spec = SpecificationUtils.build(request);
        return spec.and((root, query, cb) ->
                cb.equal(root.get("resortEntity").get("id"), resortId));
    }
}
