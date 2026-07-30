package com.example.resortbackendapplication1.address.dto.request.city;

import com.example.resortbackendapplication1.address.model.enums.CitySearchField;
import com.example.resortbackendapplication1.address.model.enums.CitySortField;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import com.example.resortbackendapplication1.commons.utils.LocaleJoinSortInfo;
import com.example.resortbackendapplication1.commons.utils.LocaleSortable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityFilterRequest extends PaginatedRequest implements Filterable, LocaleSortable {

    private String code;
    private String name;
    private Long countryId;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        throw new UnsupportedOperationException("CityFilterRequest requires a localeId — use toPredicates(root, query, cb, localeId)");
    }

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Long localeId) {
        List<Predicate> predicates = new ArrayList<>();
        for (CitySearchField field : CitySearchField.values()) {
            String value = field.getValueExtractor().apply(this);
            if (field.isLocaleField()) {
                switch (field.getSearchType()) {
                    case LIKE  -> SpecificationUtils.addJoinLikeFilter(predicates, root, query, cb,
                            field.getCollectionField(), field.getFieldName(), value, localeId, "localeEntity");
                    case EXACT -> SpecificationUtils.addJoinEqualFilter(predicates, root, query, cb,
                            field.getCollectionField(), field.getFieldName(), value, localeId, "localeEntity");
                }
            } else {
                switch (field.getSearchType()) {
                    case LIKE  -> SpecificationUtils.addLikeFilter(predicates, root, cb, field.getFieldName(), value);
                    case EXACT -> SpecificationUtils.addEqualFilter(predicates, root, cb, field.getFieldName(), value);
                }
            }
        }
        if (countryId != null) {
            predicates.add(cb.equal(root.get("countryEntity").get("id"), countryId));
        }
        return predicates;
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo() {
        throw new UnsupportedOperationException("CityFilterRequest requires a localeId — use getLocaleSortInfo(localeId)");
    }

    @Override
    public LocaleJoinSortInfo getLocaleSortInfo(Long localeId) {
        if (!CitySortField.localeSortFields().contains(getSortBy())) {
            return null;
        }
        return new LocaleJoinSortInfo("cityLocaleEntities", getSortBy(), "localeEntity", localeId, getSortDir());
    }
}
