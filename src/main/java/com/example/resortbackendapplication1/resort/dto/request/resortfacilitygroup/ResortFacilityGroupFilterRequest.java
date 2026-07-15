package com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup;

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
public class ResortFacilityGroupFilterRequest extends PaginatedRequest implements Filterable {

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        return new ArrayList<>();
    }
}
