package com.example.resortbackendapplication1.price.dto.request.priceunit;

import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.PriceUnitLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePriceUnitRequest extends PriceUnitRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotEmpty
    private Set<Long> priceScopeIds;

    @Valid
    @NotNull
    private PriceUnitLocaleRequest locale;

}
