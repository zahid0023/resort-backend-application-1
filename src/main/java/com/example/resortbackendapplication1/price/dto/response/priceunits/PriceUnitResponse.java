package com.example.resortbackendapplication1.price.dto.response.priceunits;

import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceUnitResponse {
    private final PriceUnitDto priceUnit;

    public PriceUnitResponse(PriceUnitDto priceUnit) {
        this.priceUnit = priceUnit;
    }
}
