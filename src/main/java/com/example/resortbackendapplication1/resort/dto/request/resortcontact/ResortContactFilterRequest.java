package com.example.resortbackendapplication1.resort.dto.request.resortcontact;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.model.enums.ResortContactSearchField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortContactFilterRequest extends PaginatedRequest implements Filterable {

    private Long contactTypeId;
    private Long communicationChannelId;
    private String contactValue;
    private Boolean isPrimary;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(
                this, ResortContactSearchField.values(), root, query, cb, null);
        if (contactTypeId != null) {
            predicates.add(cb.equal(root.get("contactTypeEntity").get("id"), contactTypeId));
        }
        if (communicationChannelId != null) {
            predicates.add(cb.equal(root.get("communicationChannelEntity").get("id"), communicationChannelId));
        }
        if (isPrimary != null) {
            predicates.add(cb.equal(root.get("isPrimary"), isPrimary));
        }
        return predicates;
    }
}
