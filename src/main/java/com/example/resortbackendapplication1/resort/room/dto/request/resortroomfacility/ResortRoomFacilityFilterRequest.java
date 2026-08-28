package com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomFacilitySearchField;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomFacilitySortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomFacilityFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    private Long resortRoomFacilityGroupId;
    private Long facilityId;
    private String code;
    private Boolean isHighlighted;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(this, ResortRoomFacilitySearchField.values(), root, query, cb, localeId);
        if (resortRoomFacilityGroupId != null) {
            predicates.add(cb.equal(root.get("resortRoomFacilityGroupEntity").get("id"), resortRoomFacilityGroupId));
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
        return buildLocaleSortInfo("resortRoomFacilityLocaleEntities", getSortBy(), getSortDir(), localeId, ResortRoomFacilitySortField.localeSortFields());
    }
}
