package com.example.resortbackendapplication1.address.specification;

import com.example.resortbackendapplication1.address.dto.request.city.CityFilterRequest;
import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CitySpecification {

    public Specification<CityEntity> filter(Long countryId, CityFilterRequest request) {
        return SpecificationUtils.<CityEntity>build(request)
                .and((root, query, cb) -> cb.equal(root.get("countryEntity").get("id"), countryId));
    }
}
