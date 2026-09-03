package com.example.resortbackendapplication1.mail.provider.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.MailProviderFilterRequest;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class MailProviderSpecification {

    public Specification<@NonNull MailProviderEntity> filter(MailProviderFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
