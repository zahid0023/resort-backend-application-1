package com.example.resortbackendapplication1.resortroomcategoryprice.dto.request;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomCategoryPriceFilterRequest extends PaginatedRequest implements Filterable {

    private Long priceTypeId;
    private Long priceUnitId;
    private Long currencyId;
    private LocalDate validFrom;
    private LocalDate validTo;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (priceTypeId != null) {
            predicates.add(cb.equal(root.get("priceTypeEntity").get("id"), priceTypeId));
        }
        if (priceUnitId != null) {
            predicates.add(cb.equal(root.get("priceUnitEntity").get("id"), priceUnitId));
        }
        if (currencyId != null) {
            predicates.add(cb.equal(root.get("currencyEntity").get("id"), currencyId));
        }
        if (validFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("validFrom"), validFrom));
        }
        if (validTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("validTo"), validTo));
        }
        return predicates;
    }
}
