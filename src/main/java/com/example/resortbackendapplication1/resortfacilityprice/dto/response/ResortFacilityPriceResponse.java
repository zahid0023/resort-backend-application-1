package com.example.resortbackendapplication1.resortfacilityprice.dto.response;

import com.example.resortbackendapplication1.resortfacilityprice.model.dto.ResortFacilityPriceDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityPriceResponse {
    private final ResortFacilityPriceDto data;

    public ResortFacilityPriceResponse(ResortFacilityPriceDto data) {
        this.data = data;
    }
}
