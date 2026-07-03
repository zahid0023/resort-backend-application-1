package com.example.resortbackendapplication1.price.dto.response.priceunits;

import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceUnitLocaleResponse {
    private final PriceUnitLocaleDto priceUnitLocale;

    public PriceUnitLocaleResponse(PriceUnitLocaleDto priceUnitLocale) {
        this.priceUnitLocale = priceUnitLocale;
    }
}
