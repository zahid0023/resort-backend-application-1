package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryFacilityGroupSearchField;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryFacilityGroupSortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomCategoryFacilityGroupFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private Long facilityGroupId;
    private String code;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, ResortRoomCategoryFacilityGroupSearchField.values(), root, query, cb, localeId);
        if (facilityGroupId != null) {
            predicates.add(cb.equal(root.get("facilityGroupEntity").get("id"), facilityGroupId));
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("resortRoomCategoryFacilityGroupLocaleEntities", getSortBy(), getSortDir(), localeId, ResortRoomCategoryFacilityGroupSortField.localeSortFields());
    }
}
