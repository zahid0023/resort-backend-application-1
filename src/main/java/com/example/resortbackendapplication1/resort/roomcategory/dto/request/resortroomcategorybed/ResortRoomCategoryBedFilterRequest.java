package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
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
public class ResortRoomCategoryBedFilterRequest extends PaginatedRequest implements Filterable {

    private Long bedTypeId;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (bedTypeId != null) {
            predicates.add(cb.equal(root.get("bedTypeEntity").get("id"), bedTypeId));
        }
        return predicates;
    }
}
