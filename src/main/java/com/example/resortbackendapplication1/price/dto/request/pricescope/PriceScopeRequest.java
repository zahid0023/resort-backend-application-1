package com.example.resortbackendapplication1.price.dto.request.pricescope;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceScopeRequest {

    @NotNull
    private Integer sortOrder;
}
