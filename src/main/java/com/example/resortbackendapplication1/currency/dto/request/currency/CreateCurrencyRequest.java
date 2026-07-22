package com.example.resortbackendapplication1.currency.dto.request.currency;

import com.example.resortbackendapplication1.currency.dto.request.currency.currencylocale.CreateCurrencyLocaleRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCurrencyRequest extends CurrencyRequest {

    @NotBlank
    @Size(max = 3)
    private String code;

    @NotNull
    private Long countryId;

    private List<CreateCurrencyLocaleRequest> locales;
}
