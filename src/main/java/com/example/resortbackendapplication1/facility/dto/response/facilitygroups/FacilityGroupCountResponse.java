package com.example.resortbackendapplication1.facility.dto.response.facilitygroups;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityGroupCountResponse {
    private final Long count;

    public FacilityGroupCountResponse(final Long count) {
        this.count = count;
    }
}
