package com.example.resortbackendapplication1.contact.dto.request;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.contact.model.enums.CommunicationChannelSearchField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommunicationChannelFilterRequest extends PaginatedRequest implements Filterable {

    private String code;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        for (CommunicationChannelSearchField field : CommunicationChannelSearchField.values()) {
            SpecificationUtils.addLikeFilter(predicates, root, cb,
                    field.getFieldName(), field.getValueExtractor().apply(this));
        }
        return predicates;
    }
}
