package com.example.resortbackendapplication1.currency.dto.response.currencies;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencyResponse {
    private final CurrencyDto currency;

    public CurrencyResponse(CurrencyDto currency) {
        this.currency = currency;
    }
}
