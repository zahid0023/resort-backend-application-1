package com.example.resortbackendapplication1.price.dto.response.resortfacilitypricetypes;

import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityPriceTypeResponse {
    private final FacilityPriceTypeDto data;

    public FacilityPriceTypeResponse(FacilityPriceTypeDto data) {
        this.data = data;
    }
}
