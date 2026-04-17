package com.example.resortbackendapplication1.dto.response.pricetypes;

import com.example.resortbackendapplication1.model.dto.PriceTypeDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceTypeResponse {
    private PriceTypeDto data;

    public PriceTypeResponse(PriceTypeDto priceType) {
        this.data = priceType;
    }
}
