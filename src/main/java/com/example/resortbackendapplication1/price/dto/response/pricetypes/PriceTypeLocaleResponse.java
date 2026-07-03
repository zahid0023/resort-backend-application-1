package com.example.resortbackendapplication1.price.dto.response.pricetypes;

import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceTypeLocaleResponse {
    private final PriceTypeLocaleDto priceTypeLocale;

    public PriceTypeLocaleResponse(PriceTypeLocaleDto priceTypeLocale) {
        this.priceTypeLocale = priceTypeLocale;
    }
}
