package com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilitygrouplocales;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupLocaleCountResponse {
    private final Long count;
    private final List<String> codes;

    public ResortFacilityGroupLocaleCountResponse(final Long count, final List<String> codes) {
        this.count = count;
        this.codes = codes;
    }
}
