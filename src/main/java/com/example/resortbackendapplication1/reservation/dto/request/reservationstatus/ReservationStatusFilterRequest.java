package com.example.resortbackendapplication1.reservation.dto.request.reservationstatus;

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
public class ReservationStatusFilterRequest extends PaginatedRequest implements Filterable {

    // Mirrors ReservationSourceFilterRequest: no fields classified as Filterable/Sortable, so this carries
    // only the inherited pagination fields; the active/not-deleted predicate is added by SpecificationUtils.
    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return List.of();
    }
}
