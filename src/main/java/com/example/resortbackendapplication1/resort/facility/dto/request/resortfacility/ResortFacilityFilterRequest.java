package com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.facility.model.enums.ResortFacilitySearchField;
import com.example.resortbackendapplication1.resort.facility.model.enums.ResortFacilitySortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortFacilityFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private Long resortFacilityGroupId;
    private Long facilityId;
    private String code;
    private Boolean isHighlighted;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, ResortFacilitySearchField.values(), root, query, cb, localeId);
        if (resortFacilityGroupId != null) {
            predicates.add(cb.equal(root.get("resortFacilityGroupEntity").get("id"), resortFacilityGroupId));
        }
        if (facilityId != null) {
            predicates.add(cb.equal(root.get("facilityEntity").get("id"), facilityId));
        }
        if (isHighlighted != null) {
            predicates.add(cb.equal(root.get("isHighlighted"), isHighlighted));
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("resortFacilityLocaleEntities", getSortBy(), getSortDir(), localeId, ResortFacilitySortField.localeSortFields());
    }
}
