package com.example.resortbackendapplication1.price.dto.request.pricetype;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.enums.PriceTypeSearchField;
import com.example.resortbackendapplication1.price.model.enums.PriceTypeSortField;
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
public class PriceTypeFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private String code;
    private String name;
    private Set<String> priceScopeCodes;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, PriceTypeSearchField.values(), root, query, cb, localeId);
        if (priceScopeCodes != null && !priceScopeCodes.isEmpty()) {
            // Correlated EXISTS subquery instead of a join — a join against this to-many
            // collection would duplicate parent rows and require query.distinct(true),
            // which Postgres rejects when combined with an ORDER BY on a joined locale
            // column that isn't in the select list (see SpecificationUtils.addJoinSort).
            Subquery<Long> scopeAssignmentExists = query.subquery(Long.class);
            Root<PriceTypeScopeAssignmentEntity> scopeAssignmentRoot = scopeAssignmentExists.from(PriceTypeScopeAssignmentEntity.class);
            scopeAssignmentExists.select(scopeAssignmentRoot.get("id"))
                    .where(
                            cb.equal(scopeAssignmentRoot.get("priceTypeEntity"), root),
                            scopeAssignmentRoot.get("priceScopeEntity").get("code").in(priceScopeCodes),
                            cb.isTrue(scopeAssignmentRoot.get("isActive")),
                            cb.isFalse(scopeAssignmentRoot.get("isDeleted"))
                    );
            predicates.add(cb.exists(scopeAssignmentExists));
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("priceTypeLocaleEntities", getSortBy(), getSortDir(), localeId, PriceTypeSortField.localeSortFields());
    }
}
