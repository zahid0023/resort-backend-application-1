package com.example.resortbackendapplication1.resort.contact.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.contact.dto.request.resortcontact.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resort.contact.model.entity.ResortContactEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortContactSpecification {

    public Specification<@NonNull ResortContactEntity> filter(ResortContactFilterRequest request, Long resortId) {
        Specification<@NonNull ResortContactEntity> base = SpecificationUtils.build(request);
        Specification<@NonNull ResortContactEntity> resortScope =
                (root, query, cb) -> cb.equal(root.get("resortEntity").get("id"), resortId);
        return base.and(resortScope);
    }
}
