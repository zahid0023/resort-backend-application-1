package com.example.resortbackendapplication1.reservation.specification;

import com.example.resortbackendapplication1.commons.utils.SpecificationUtils;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.ReservationStatusFilterRequest;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ReservationStatusSpecification {

    public Specification<@NonNull ReservationStatusEntity> filter(ReservationStatusFilterRequest request, Long localeId) {
        return SpecificationUtils.build(request, localeId);
    }
}
