package com.example.resortbackendapplication1.facility.dto.request.facility;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.facility.model.entity.FacilityFacilityGroupAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySortField;
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
public class FacilityFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private String code;
    private Long facilityGroupId;
    private Set<String> facilityScopeCodes;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, FacilitySearchField.values(), root, query, cb, localeId);
        if (facilityGroupId != null) {
            // Correlated EXISTS subquery instead of a join — a join against this to-many
            // collection would duplicate parent rows and require query.distinct(true),
            // which Postgres rejects when combined with an ORDER BY on a joined locale
            // column that isn't in the select list (see SpecificationUtils.addJoinSort).
            Subquery<Long> assignmentExists = query.subquery(Long.class);
            Root<FacilityFacilityGroupAssignmentEntity> assignmentRoot = assignmentExists.from(FacilityFacilityGroupAssignmentEntity.class);
            assignmentExists.select(assignmentRoot.get("id"))
                    .where(
                            cb.equal(assignmentRoot.get("facilityEntity"), root),
                            cb.equal(assignmentRoot.get("facilityGroupEntity").get("id"), facilityGroupId),
                            cb.isTrue(assignmentRoot.get("isActive")),
                            cb.isFalse(assignmentRoot.get("isDeleted"))
                    );
            predicates.add(cb.exists(assignmentExists));
        }
        if (facilityScopeCodes != null && !facilityScopeCodes.isEmpty()) {
            // Correlated EXISTS subquery instead of a join — a join against this to-many
            // collection would duplicate parent rows and require query.distinct(true),
            // which Postgres rejects when combined with an ORDER BY on a joined locale
            // column that isn't in the select list (see SpecificationUtils.addJoinSort).
            Subquery<Long> scopeAssignmentExists = query.subquery(Long.class);
            Root<FacilityScopeAssignmentEntity> scopeAssignmentRoot = scopeAssignmentExists.from(FacilityScopeAssignmentEntity.class);
            scopeAssignmentExists.select(scopeAssignmentRoot.get("id"))
                    .where(
                            cb.equal(scopeAssignmentRoot.get("facilityEntity"), root),
                            scopeAssignmentRoot.get("facilityScopeEntity").get("code").in(facilityScopeCodes),
                            cb.isTrue(scopeAssignmentRoot.get("isActive")),
                            cb.isFalse(scopeAssignmentRoot.get("isDeleted"))
                    );
            predicates.add(cb.exists(scopeAssignmentExists));
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("facilityLocaleEntities", getSortBy(), getSortDir(), localeId, FacilitySortField.localeSortFields());
    }
}
