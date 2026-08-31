package com.example.resortbackendapplication1.bookingsource.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.BookingSourceFilterRequest;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class BookingSourceSpecification {

    public Specification<@NonNull BookingSourceEntity> filter(BookingSourceFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
