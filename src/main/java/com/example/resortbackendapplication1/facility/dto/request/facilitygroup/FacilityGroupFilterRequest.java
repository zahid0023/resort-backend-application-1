package com.example.resortbackendapplication1.facility.dto.request.facilitygroup;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class FacilityGroupFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private String code;
    private String name;
    private Set<Long> facilityScopeIds;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, FacilityGroupSearchField.values(), root, query, cb, localeId);
        if (facilityScopeIds != null && !facilityScopeIds.isEmpty()) {
            Join<?, ?> assignmentJoin = root.join("facilityGroupScopeAssignmentEntities");
            predicates.add(assignmentJoin.get("facilityScopeEntity").get("id").in(facilityScopeIds));
            predicates.add(cb.isTrue(assignmentJoin.get("isActive")));
            predicates.add(cb.isFalse(assignmentJoin.get("isDeleted")));
            query.distinct(true);
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("facilityGroupLocaleEntities", getSortBy(), getSortDir(), localeId, FacilityGroupSortField.localeSortFields());
    }
}
