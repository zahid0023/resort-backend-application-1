package com.example.resortbackendapplication1.price.dto.response.pricetypescopes;

import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceTypeScopeResponse {
    private final PriceTypeScopeDto data;

    public PriceTypeScopeResponse(PriceTypeScopeDto data) {
        this.data = data;
    }
}
