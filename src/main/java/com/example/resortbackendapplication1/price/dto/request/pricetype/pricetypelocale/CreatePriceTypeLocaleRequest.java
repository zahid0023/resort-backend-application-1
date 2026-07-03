package com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePriceTypeLocaleRequest extends PriceTypeLocaleRequest {

    @NotNull
    private Long localeId;
}
