package com.example.resortbackendapplication1.reservation.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.ReservationSourceFilterRequest;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ReservationSourceSpecification {

    public Specification<@NonNull ReservationSourceEntity> filter(ReservationSourceFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
