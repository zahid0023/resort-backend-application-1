package com.example.resortbackendapplication1.contact.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.contact.dto.request.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CommunicationChannelSpecification {

    public Specification<@NonNull CommunicationChannelEntity> filter(CommunicationChannelFilterRequest request) {
        return SpecificationUtils.build(request);
    }
}
