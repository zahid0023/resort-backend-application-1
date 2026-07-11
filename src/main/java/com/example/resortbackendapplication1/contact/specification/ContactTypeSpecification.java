package com.example.resortbackendapplication1.contact.specification;

import com.example.resortbackendapplication1.contact.dto.request.ContactTypeFilterRequest;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ContactTypeSpecification {

    public Specification<@NonNull ContactTypeEntity> filter(ContactTypeFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
