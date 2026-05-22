package com.example.resortbackendapplication1.dto.response.countries;

import com.example.resortbackendapplication1.model.dto.CountryLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CountryLocaleResponse {
    private final CountryLocaleDto countryLocale;

    public CountryLocaleResponse(CountryLocaleDto countryLocale) {
        this.countryLocale = countryLocale;
    }
}
