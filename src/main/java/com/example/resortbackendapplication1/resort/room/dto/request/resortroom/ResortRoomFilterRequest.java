package com.example.resortbackendapplication1.resort.room.dto.request.resortroom;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleRequiredFilterable;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomSearchField;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomSortField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomFilterRequest extends PaginatedRequest implements LocaleRequiredFilterable, LocaleSortable {

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        return SpecificationUtils.buildSearchPredicates(this, ResortRoomSearchField.values(), root, query, cb, localeId);
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        return buildLocaleSortInfo("resortRoomLocaleEntities", getSortBy(), getSortDir(), localeId, ResortRoomSortField.localeSortFields());
    }
}
