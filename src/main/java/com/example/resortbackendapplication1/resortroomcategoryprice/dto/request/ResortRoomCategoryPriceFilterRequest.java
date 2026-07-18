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
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (priceTypeId != null) {
            predicates.add(cb.equal(root.get("priceTypeEntity").get("id"), priceTypeId));
        }
        if (priceUnitId != null) {
            predicates.add(cb.equal(root.get("priceUnitEntity").get("id"), priceUnitId));
        }
        if (validFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("validFrom"), validFrom));
        }
        if (validTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("validTo"), validTo));
        }
        if (monday != null) {
            predicates.add(cb.equal(root.get("monday"), monday));
        }
        if (tuesday != null) {
            predicates.add(cb.equal(root.get("tuesday"), tuesday));
        }
        if (wednesday != null) {
            predicates.add(cb.equal(root.get("wednesday"), wednesday));
        }
        if (thursday != null) {
            predicates.add(cb.equal(root.get("thursday"), thursday));
        }
        if (friday != null) {
            predicates.add(cb.equal(root.get("friday"), friday));
        }
        if (saturday != null) {
            predicates.add(cb.equal(root.get("saturday"), saturday));
        }
        if (sunday != null) {
            predicates.add(cb.equal(root.get("sunday"), sunday));
        }
        return predicates;
    }
}
