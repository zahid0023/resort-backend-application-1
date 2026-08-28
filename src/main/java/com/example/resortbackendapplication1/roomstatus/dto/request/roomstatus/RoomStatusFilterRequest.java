package com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus;

import com.example.resortbackendapplication1.roomstatus.model.enums.RoomStatusSearchField;
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
public class RoomStatusFilterRequest extends PaginatedRequest implements Filterable {

    private String code;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return SpecificationUtils.buildSearchPredicates(this, RoomStatusSearchField.values(), root, query, cb, null);
    }
}
