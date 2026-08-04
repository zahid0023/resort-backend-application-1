package com.example.resortbackendapplication1.contact.specification;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CommunicationChannelSpecification {

    public Specification<@NonNull CommunicationChannelEntity> filter(CommunicationChannelFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
