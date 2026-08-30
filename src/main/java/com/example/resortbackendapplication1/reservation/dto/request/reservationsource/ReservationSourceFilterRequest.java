package com.example.resortbackendapplication1.reservation.dto.request.reservationsource;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReservationSourceFilterRequest extends PaginatedRequest implements Filterable {

    // No fields on ReservationSource were classified as Filterable/Sortable, so this filter carries only the
    // inherited pagination fields; the mandatory active/not-deleted predicate is added by SpecificationUtils.
    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return List.of();
    }
}
