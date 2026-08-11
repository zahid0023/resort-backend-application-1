package com.example.resortbackendapplication1.price.dto.request.pricetype;

import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.PriceTypeLocaleRequest;
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
public class CreatePriceTypeRequest extends PriceTypeRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotEmpty
    private Set<Long> priceScopeIds;

    @Valid
    @NotNull
    private PriceTypeLocaleRequest locale;

}
