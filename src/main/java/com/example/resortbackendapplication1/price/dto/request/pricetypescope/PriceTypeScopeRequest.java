package com.example.resortbackendapplication1.price.dto.request.pricetypescope;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceTypeScopeRequest {

    @NotNull
    private Integer sortOrder;
}
