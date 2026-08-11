package com.example.resortbackendapplication1.facility.dto.request.facility;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FacilityFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private String code;
    private Long facilityGroupId;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, FacilitySearchField.values(), root, query, cb, localeId);
        if (facilityGroupId != null) {
            Join<?, ?> assignmentJoin = root.join("facilityFacilityGroupAssignmentEntities");
            predicates.add(cb.equal(assignmentJoin.get("facilityGroupEntity").get("id"), facilityGroupId));
            predicates.add(cb.isTrue(assignmentJoin.get("isActive")));
            predicates.add(cb.isFalse(assignmentJoin.get("isDeleted")));
            query.distinct(true);
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("facilityLocaleEntities", getSortBy(), getSortDir(), localeId, FacilitySortField.localeSortFields());
    }
}
