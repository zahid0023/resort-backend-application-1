package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryFacilitySearchField;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryFacilitySortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomCategoryFacilityFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private Long resortRoomCategoryFacilityGroupId;
    private Long facilityId;
    private String code;
    private Boolean isHighlighted;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, ResortRoomCategoryFacilitySearchField.values(), root, query, cb, localeId);
        if (resortRoomCategoryFacilityGroupId != null) {
            predicates.add(cb.equal(root.get("resortRoomCategoryFacilityGroupEntity").get("id"), resortRoomCategoryFacilityGroupId));
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
        return buildLocaleSortInfo("resortRoomCategoryFacilityLocaleEntities", getSortBy(), getSortDir(), localeId, ResortRoomCategoryFacilitySortField.localeSortFields());
    }
}
