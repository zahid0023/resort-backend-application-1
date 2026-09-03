package com.example.resortbackendapplication1.mail.provider.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.MailProviderConfigFilterRequest;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class MailProviderConfigSpecification {

    public Specification<@NonNull MailProviderConfigEntity> filter(MailProviderConfigFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
