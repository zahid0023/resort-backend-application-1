package com.example.resortbackendapplication1.resortroomcategoryprice.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceFilterRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ResortRoomCategoryPriceSpecification {

    public Specification<@NonNull ResortRoomCategoryPriceEntity> filter(
            ResortRoomCategoryPriceFilterRequest request, Long resortRoomCategoryId) {
        Specification<ResortRoomCategoryPriceEntity> spec = SpecificationUtils.build(request);
        return spec.and((root, query, cb) ->
                cb.equal(root.get("resortRoomCategoryEntity").get("id"), resortRoomCategoryId));
    }
}
