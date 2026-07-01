package com.example.resortbackendapplication1.facility.dto.response.facilitygroups;

import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityGroupLocaleResponse {
    private final FacilityGroupLocaleDto facilityGroupLocale;

    public FacilityGroupLocaleResponse(FacilityGroupLocaleDto facilityGroupLocale) {
        this.facilityGroupLocale = facilityGroupLocale;
    }
}
