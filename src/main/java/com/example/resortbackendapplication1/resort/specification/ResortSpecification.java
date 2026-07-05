package com.example.resortbackendapplication1.resort.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortSpecification {

    public Specification<@NonNull ResortEntity> filter(ResortFilterRequest request) {
        return SpecificationUtils.build(request);
    }

    public Specification<@NonNull ResortEntity> filter(ResortFilterRequest request, Long userId) {
        Specification<@NonNull ResortEntity> spec = SpecificationUtils.build(request);
        return spec.and(forUser(userId));
    }

    private Specification<@NonNull ResortEntity> forUser(Long userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            var join = root.join("resortUserEntities", JoinType.INNER);
            return cb.and(
                    cb.equal(join.get("userEntity").get("id"), userId),
                    cb.equal(join.get("isActive"), true),
                    cb.equal(join.get("isDeleted"), false)
            );
        };
    }
}
