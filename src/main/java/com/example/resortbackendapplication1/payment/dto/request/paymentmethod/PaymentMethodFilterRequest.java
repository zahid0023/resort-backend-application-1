package com.example.resortbackendapplication1.payment.dto.request.paymentmethod;

import com.example.resortbackendapplication1.payment.model.enums.PaymentMethodSearchField;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentMethodFilterRequest extends PaginatedRequest implements Filterable {

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return SpecificationUtils.buildSearchPredicates(this, PaymentMethodSearchField.values(), root, query, cb, null);
    }
}
