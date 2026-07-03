package com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePriceUnitLocaleRequest extends PriceUnitLocaleRequest {

    @NotNull
    private Long localeId;
}
