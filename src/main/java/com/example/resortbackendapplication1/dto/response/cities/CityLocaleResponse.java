package com.example.resortbackendapplication1.dto.response.cities;

import com.example.resortbackendapplication1.model.dto.CityLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CityLocaleResponse {
    private final CityLocaleDto cityLocale;

    public CityLocaleResponse(CityLocaleDto cityLocale) {
        this.cityLocale = cityLocale;
    }
}
