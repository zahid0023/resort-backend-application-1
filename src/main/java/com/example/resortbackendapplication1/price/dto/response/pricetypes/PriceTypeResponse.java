package com.example.resortbackendapplication1.price.dto.response.pricetypes;

import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceTypeResponse {
    private final PriceTypeDto data;

    public PriceTypeResponse(PriceTypeDto data) {
        this.data = data;
    }
}
