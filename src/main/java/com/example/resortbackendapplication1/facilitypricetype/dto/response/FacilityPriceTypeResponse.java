package com.example.resortbackendapplication1.facilitypricetype.dto.response;

import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityPriceTypeResponse {
    private final FacilityPriceTypeDto data;

    public FacilityPriceTypeResponse(FacilityPriceTypeDto facilityPriceType) {
        this.data = facilityPriceType;
    }
}
