package com.example.resortbackendapplication1.image.hosting.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.image.hosting.dto.request.imagehostingproviderconfig.ImageHostingProviderConfigFilterRequest;
import com.example.resortbackendapplication1.image.hosting.model.entity.ImageHostingProviderConfigEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ImageHostingProviderConfigSpecification {

    public Specification<@NonNull ImageHostingProviderConfigEntity> filter(ImageHostingProviderConfigFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
