package com.example.resortbackendapplication1.price.dto.response.pricescopes;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceScopeCountResponse {
    private final Long count;
    private final List<String> codes;

    public PriceScopeCountResponse(final Long count, final List<String> codes) {
        this.count = count;
        this.codes = codes;
    }
}
