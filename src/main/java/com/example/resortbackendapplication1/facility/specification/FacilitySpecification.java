package com.example.resortbackendapplication1.facility.specification;

import com.example.resortbackendapplication1.facility.dto.request.facilities.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeCode;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FacilitySpecification {

    public Specification<@NonNull FacilityEntity> filter(FacilityFilterRequest request, Long facilityGroupId, FacilityScopeCode scopeCode) {
        Specification<FacilityEntity> spec = SpecificationUtils.build(request);
        if (facilityGroupId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("facilityGroupEntity").get("id"), facilityGroupId));
        }
        if (scopeCode != null) {
            spec = spec.and((root, query, cb) -> {
                var assignmentJoin = root.join("facilityScopeAssignmentEntities", JoinType.INNER);
                return cb.and(
                        cb.equal(assignmentJoin.get("facilityScopeEntity").get("code"), scopeCode.name()),
                        cb.isFalse(assignmentJoin.get("isDeleted"))
                );
            });
        }
        return spec;
    }
}
