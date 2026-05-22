package com.example.resortbackendapplication1.dto.response.facilities;

import com.example.resortbackendapplication1.model.dto.FacilityLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityLocaleResponse {
    private final FacilityLocaleDto facilityLocale;

    public FacilityLocaleResponse(FacilityLocaleDto facilityLocale) {
        this.facilityLocale = facilityLocale;
    }
}
