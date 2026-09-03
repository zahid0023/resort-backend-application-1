package com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resort.mail.model.enums.ResortMailConfigSearchField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortMailConfigFilterRequest extends PaginatedRequest implements Filterable {

    private Long mailProviderId;
    private String name;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = SpecificationUtils.buildSearchPredicates(
                this, ResortMailConfigSearchField.values(), root, query, cb, null);
        if (mailProviderId != null) {
            predicates.add(cb.equal(root.get("mailProviderEntity").get("id"), mailProviderId));
        }
        return predicates;
    }
}
