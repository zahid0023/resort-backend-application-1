package com.example.resortbackendapplication1.facility.dto.request.facilitygroup;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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
            // Correlated EXISTS subquery instead of a join — a join against this to-many
            // collection would duplicate parent rows and require query.distinct(true),
            // which Postgres rejects when combined with an ORDER BY on a joined locale
            // column that isn't in the select list (see SpecificationUtils.addJoinSort).
            Subquery<Long> assignmentExists = query.subquery(Long.class);
            Root<FacilityGroupScopeAssignmentEntity> assignmentRoot = assignmentExists.from(FacilityGroupScopeAssignmentEntity.class);
            assignmentExists.select(assignmentRoot.get("id"))
                    .where(
                            cb.equal(assignmentRoot.get("facilityGroupEntity"), root),
                            assignmentRoot.get("facilityScopeEntity").get("id").in(facilityScopeIds),
                            cb.isTrue(assignmentRoot.get("isActive")),
                            cb.isFalse(assignmentRoot.get("isDeleted"))
                    );
            predicates.add(cb.exists(assignmentExists));
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("facilityGroupLocaleEntities", getSortBy(), getSortDir(), localeId, FacilityGroupSortField.localeSortFields());
    }
}
