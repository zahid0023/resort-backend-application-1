package com.example.resortbackendapplication1.facility.specification;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeCode;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilityGroupSpecification {

    public Specification<@NonNull FacilityGroupEntity> filter(FacilityGroupFilterRequest request,
                                                              FacilityScopeCode scopeCode) {
        Specification<@NonNull FacilityGroupEntity> spec = SpecificationUtils.build(request);

        if (scopeCode != null) {
            spec = spec.and(byScopeCode(scopeCode));
        }

        return spec;
    }

    private Specification<@NonNull FacilityGroupEntity> byScopeCode(FacilityScopeCode scopeCode) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Object, Object> assignments = root.join("facilityGroupScopeAssignmentEntities", JoinType.INNER);
            Join<Object, Object> scope = assignments.join("facilityScopeEntity", JoinType.INNER);
            return cb.and(
                    cb.equal(scope.get("code"), scopeCode.name()),
                    cb.equal(assignments.get("isActive"), true),
                    cb.equal(assignments.get("isDeleted"), false)
            );
        };
    }
}
