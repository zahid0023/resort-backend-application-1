package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategorySearchField;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategorySortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomCategoryFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        return SpecificationUtils.buildSearchPredicates(this, ResortRoomCategorySearchField.values(), root, query, cb, localeId);
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("resortRoomCategoryLocaleEntities", getSortBy(), getSortDir(), localeId, ResortRoomCategorySortField.localeSortFields());
    }
}
