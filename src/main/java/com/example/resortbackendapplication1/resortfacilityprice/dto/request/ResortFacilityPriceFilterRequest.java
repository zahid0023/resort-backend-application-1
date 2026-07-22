package com.example.resortbackendapplication1.resortfacilityprice.dto.request;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortFacilityPriceFilterRequest extends PaginatedRequest implements Filterable {

    private Long priceUnitId;
    private Long currencyId;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (priceUnitId != null) {
            predicates.add(cb.equal(root.get("priceUnitEntity").get("id"), priceUnitId));
        }
        if (currencyId != null) {
            predicates.add(cb.equal(root.get("currencyEntity").get("id"), currencyId));
        }
        return predicates;
    }
}
