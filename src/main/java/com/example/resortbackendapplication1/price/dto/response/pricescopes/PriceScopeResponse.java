package com.example.resortbackendapplication1.price.dto.response.pricescopes;

import com.example.resortbackendapplication1.price.model.dto.PriceScopeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceScopeResponse {
    private final PriceScopeDto data;

    public PriceScopeResponse(PriceScopeDto data) {
        this.data = data;
    }
}
