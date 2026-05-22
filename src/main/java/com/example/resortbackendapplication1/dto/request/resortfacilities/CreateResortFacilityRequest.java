package com.example.resortbackendapplication1.dto.request.resortfacilities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityRequest extends ResortFacilityRequest {

    @NotNull(message = "facility_id must not be null")
    private Long facilityId;

    @NotNull(message = "sort_order must not be null")
    @Override
    public Integer getSortOrder() {
        return super.getSortOrder();
    }
}
