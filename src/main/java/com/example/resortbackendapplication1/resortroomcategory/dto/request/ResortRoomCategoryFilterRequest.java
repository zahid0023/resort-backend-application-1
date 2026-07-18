package com.example.resortbackendapplication1.resortroomcategory.dto.request;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.utils.Filterable;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortroomcategory.model.enums.ResortRoomCategorySearchField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomCategoryFilterRequest extends PaginatedRequest implements Filterable {

    private String code;
    private Long roomCategoryId;
    private Boolean isExtraBedAllowed;
    private Boolean isSmokingAllowed;
    private Boolean isPetsAllowed;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        for (ResortRoomCategorySearchField field : ResortRoomCategorySearchField.values()) {
            SpecificationUtils.addLikeFilter(predicates, root, cb,
                    field.getFieldName(), field.getValueExtractor().apply(this));
        }
        if (roomCategoryId != null) {
            predicates.add(cb.equal(root.get("roomCategoryEntity").get("id"), roomCategoryId));
        }
        if (isExtraBedAllowed != null) {
            predicates.add(cb.equal(root.get("isExtraBedAllowed"), isExtraBedAllowed));
        }
        if (isSmokingAllowed != null) {
            predicates.add(cb.equal(root.get("isSmokingAllowed"), isSmokingAllowed));
        }
        if (isPetsAllowed != null) {
            predicates.add(cb.equal(root.get("isPetsAllowed"), isPetsAllowed));
        }
        return predicates;
    }
}
