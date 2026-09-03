package com.example.resortbackendapplication1.resort.mail.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.ResortMailConfigFilterRequest;
import com.example.resortbackendapplication1.resort.mail.model.entity.ResortMailConfigEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortMailConfigSpecification {

    public Specification<@NonNull ResortMailConfigEntity> filter(ResortMailConfigFilterRequest request, Long resortId) {
        Specification<@NonNull ResortMailConfigEntity> base = SpecificationUtils.build(request);
        Specification<@NonNull ResortMailConfigEntity> resortScope =
                (root, query, cb) -> cb.equal(root.get("resortEntity").get("id"), resortId);
        return base.and(resortScope);
    }
}
